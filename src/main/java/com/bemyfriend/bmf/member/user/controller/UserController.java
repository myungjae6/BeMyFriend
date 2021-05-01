package com.bemyfriend.bmf.member.user.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.bemyfriend.bmf.common.code.ConfigCode;
import com.bemyfriend.bmf.common.code.ErrorCode;
import com.bemyfriend.bmf.common.exception.ToAlertException;
import com.bemyfriend.bmf.common.random.RandomString;
import com.bemyfriend.bmf.common.util.file.FileVo;
import com.bemyfriend.bmf.member.user.model.service.UserService;
import com.bemyfriend.bmf.member.user.model.vo.User;
import com.bemyfriend.bmf.member.user.model.vo.UserHopeService;
import com.bemyfriend.bmf.member.user.validator.UserValidator;



@Controller
@RequestMapping("member/user")
public class UserController {
	
	
	private final UserService userService;

	private final UserValidator userValidator;
	
	
	
	public UserController(UserService userService,UserValidator userValidator) {
		this.userService = userService; //의존성 주입
		this.userValidator = userValidator;
	}
	
	
	
	@InitBinder(value="user")
	public void InitBinder(WebDataBinder webDataBinder) {
		// WebDataBinder : Controller 메소드의 파라미터에 데이터를 바인드 해주는 역할을 하는 클래스
		webDataBinder.addValidators(userValidator);
		
		//validator를 만들고, Controller에 i@nitBinder가 붙은 메소드를 만들고 Controller 파라미터에 값을 실제로 바인딩해주는
		//webDataBinder를 받아와서 파라미터값을 바인딩 하기 전에 수행시킬 validator로 우리가 만든 userValidator 등록
	}

	
	
	//join.jsp로 이동
	@GetMapping("join")
	public String join() { // 회원가입페이지 이동
		return "member/user/join";
	}
	
	
	
	
	
	//해당 id의 존재유무 확인
	@GetMapping("idcheck")
	@ResponseBody //메소드 반환값 응답바디에 담아주기
	public String idCheck(String userId) { //@RequestParam 생략 상태
		
		if(userService.selectMemberById(userId) != null) {
			return "fail"; 
		}
		return "available";
	}
	
	
	
	
	
	//회원가입 버튼 클릭시 mail 전송
	@PostMapping("mailauth")
	public String authenticateEmail(@Valid User persistUser
									,Errors errors
									,HttpSession session
									,Model model) {
		if(errors.hasErrors()) {
			//에러가 하나라도 발견될 시에는 join으로 다시 되돌리기 
			return "member/user/join";
		}
		
		if(persistUser.getTwoadd() != null) {
			String userAdd = persistUser.getUserAdd() + " " +persistUser.getTwoadd();
			persistUser.setUserAdd(userAdd);
		}
		
		
		
		String authPath = UUID.randomUUID().toString(); //유니크한 아이디 생성
		session.setAttribute("authPath", authPath);
		session.setAttribute("persistUser", persistUser);
		

		userService.authenticateEmail(persistUser, authPath);
		model.addAttribute("alertMsg", "이메일이 발송되었습니다.");
		model.addAttribute("url",ConfigCode.DOMAIN+"/index");
		
		return "common/result";
		
		
	}
	
	
	
	
	
	//전송된 메일이 확인되면 회원가입 진행완료
	@GetMapping("joinimpl/{authPath}")
	public String joinImpl(@PathVariable("authPath") String urlPath
							,HttpSession session
							,@SessionAttribute("authPath") String sessionPath //@SessionAttribute : session에 setAttribute로 설정해둔 값
							,@SessionAttribute("persistUser") User persistUser
							,Model model) {
		

		if(!urlPath.contentEquals(sessionPath)) {
			
			throw new ToAlertException(ErrorCode.AUTH02);
		}

		userService.insertUser(persistUser);
	

		//세션 만료시키기
		session.removeAttribute("persistUser");
		
		model.addAttribute("alertMsg", "회원가입이 완료되었습니다.");
		model.addAttribute("url","/member/user/login");
		return "common/result";
		
	}
	
	
	@GetMapping("findinfo")
	public String findInfo() {
		
		return "/member/user/findinfo";
		
	}
	
	
	
	
	//회원 아이디 찾기
	@GetMapping("finduserid")
	public String findUserId(String userName
							,String userTell
							,Model model ) {
		
		
		String userId = userService.findUserId(userName, userTell);
		if(userId == null) {
			
			model.addAttribute("fail","조회되는 회원이 없습니다.");
		}
			
			
		model.addAttribute("userId",userId);
		return "member/find_result";
	}
	
	
	

	
	//회원 비밀번호 찾기
	@GetMapping("finduserpw")
	public String findUserPw(String userId
							,String userMail
							,Model model) {
		
		
		User user = userService.findUserPw(userId, userMail);
		
		//유저가 있다면
		if(user != null) {
			//새로운 비밀번호 생성
			String randomStr= new RandomString().randomStr(3);
		
			//새로운 객체에 담아서
			User updateUser = new User();
			updateUser.setUserId(userId);
			updateUser.setUserPw(randomStr);
			
			
			List<MultipartFile> files =new ArrayList<MultipartFile>();
			//업데이트 문 돌리기(비밀번호 인코딩 진행됨)
			userService.updateUserInfo(updateUser);
			model.addAttribute("userPw", randomStr);
			
			return "member/find_result";
			
		
		}
		
		model.addAttribute("fail","조회되는 회원이 없습니다.");
		
		return "member/find_result";
	}	
	

	
	
	//로그인 페이지로 이동
	@GetMapping("login")
	public String login() {
	
		return "/member/user/login";
	}
	
	
	
	
	
	
	//로그인 정보기반 로그인 완료
	@PostMapping("loginimpl")
	@ResponseBody		//form-json으로 넘겼기때문에 ! //session에 저장해야 함
	public String loginmpl(@RequestBody User user
							, HttpSession session) {

		User userMember = userService.memberAuthenticate(user);
		
		// 없는 회원이라면
		if(userMember == null) {
			return "fail";
	
		}else { //로그인 성공시
			// 가지고 있는 이미지 파일을 찾아내기
			FileVo file = userService.selectUserFile(userMember.getUserIdx());
			// 가지고 있는 서비스 정보 가져오기
			UserHopeService service = userService.selectUserService(userMember.getUserId());
			//이미지 파일이 있다면 파일정보 세션에 저장
			if(file != null) {
			session.setAttribute("file", file);
			}
			//서비스 있다면 서비스정보 세션에 저장
			if(service != null) {
			session.setAttribute("service", service);
			}
			
			//로그인 정보 세션 저장
			session.setAttribute("userMember", userMember);
			session.setAttribute("memberId", userMember.getUserId());
			session.setAttribute("memberName", userMember.getUserName());
			return "success";
		}
	}

	
	
	
	
	//로그아웃하기
	@GetMapping("logout")
	public String logout(HttpSession session) {
		
		userService.userLogout(session);
		
		return "/index";
	}
	
	
	
	
	
	//마이페이지 이동
	@GetMapping("mypage")
	public String mypage(HttpSession session) {
			
		return "member/user/mypage";
	}
	
	
	
	
	//마이페이지 수정하기
	@PostMapping("updateinfo")
	public String updateUserInfo(@RequestParam MultipartFile file
								, UserHopeService serviceInfo
								, User user
								, HttpSession session
								, Model model){
		
	
		
		//넘어오는 파일의 사이즈가 0이 아닌경우에만 보내기
		if(file.getSize() != 0) {
			userService.uploadFile(file, session);
		}
		
		int result =  userService.updateUserInfo(user);
		
		// 서비스 업로드/업데이트 프로시저 진행
		int serviceRes = userService.uploadUserService(serviceInfo);
		
		
		if(result > 0) {
			
			System.out.println("service 등록 완료 !");
			User userMember = userService.selectMemberById(user.getUserId());
			FileVo updateFile = userService.selectUserFile(userMember.getUserIdx());
			UserHopeService service = userService.selectUserService(user.getUserId());
			session.setAttribute("userMember", userMember);
			session.setAttribute("file", updateFile);
			session.setAttribute("service", service);
			

			model.addAttribute("alertMsg", "회원정보 수정이 성공하였습니다.");
			model.addAttribute("url",ConfigCode.DOMAIN+"/member/user/mypage");
			return "common/result";
			
		}else {

			model.addAttribute("alertMsg", "회원정보 수정이 실패하였습니다.");
			model.addAttribute("url",ConfigCode.DOMAIN+"member/user/mypage");
			return "common/result";
		}
		
		
	}

	
	
	//회원탈퇴하기
	@GetMapping("withdraw")	
	@ResponseBody				//생략가능, 파라미터값과 	변수가 동일하기에.
	public String withdrawUser(@RequestParam String userId
								,HttpSession session) {
			
		int result = userService.withdrawUser(userId);
		
		if(result > 0) {
			//탈퇴 성공시 - 세션 삭제 및  index로 이동
			userService.userLogout(session);
			return "success";
		}
			//탈퇴 실패시
			return "fail";
		
	}
	
	
	
	
	//지원하기
	@GetMapping("apply")
	public String applyProcess() {
		
		
		
		
		
		
		return null;
	}
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
