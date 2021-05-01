package com.bemyfriend.bmf.recruitment.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bemyfriend.bmf.member.company.model.service.CompanyService;
import com.bemyfriend.bmf.member.company.model.vo.CompanySupport;
import com.bemyfriend.bmf.member.user.model.service.ResumeService;
import com.bemyfriend.bmf.recruitment.model.service.impl.RecruServiceImpl;
import com.bemyfriend.bmf.recruitment.model.vo.Recruitment;

@Controller
@RequestMapping("recruitment")
public class RecruController {

	@Autowired
	private RecruServiceImpl recruService;
	@Autowired
	private ResumeService resumeService;
	@Autowired
	private CompanyService comService;
	
	// 게시판 메인
	@GetMapping("recruitment")
	public String list(
			@RequestParam(defaultValue = "1")int page, Model model, @ModelAttribute("recruInfo") Recruitment recruitment, HttpSession session) {

		Date today = new Date();	
		SimpleDateFormat sdfm = new SimpleDateFormat("yyyy.MM.dd");
		String now = sdfm.format(today);
		
		
			model.addAllAttributes(recruService.selectRecruList(page));
			model.addAttribute("page",page);
			return "recruitment/recruitment";
		}
	
	// 게시글 작성 
	
	@GetMapping("recruitmentForm")
	public String listForm()
	{
		
		return "recruitment/recruitmentForm";
	}
	
	//공고 올리기
	@PostMapping("uploadForm")
	public String write(@RequestParam List<MultipartFile> files,
			@ModelAttribute Recruitment recruitment,
			HttpSession session)
	{
		String comId = (String)session.getAttribute("comMemberId");
		String comName = (String)session.getAttribute("comMemberName");
		String comAddress = (String)session.getAttribute("comMemberAddress");
		String typeIdx = "r" + String.valueOf(session.getAttribute("comIdx"));
		
		recruitment.setComId(comId);
		recruitment.setComName(comName);
		recruitment.setJobLocation(comAddress);
		recruitment.setTypeIdx(typeIdx);
		System.out.println(recruitment);
		System.out.println("다시 게시판으로 redirect");
		
		recruService.insertRecru(recruitment, files);
				return "redirect:/recruitment/recruitment";
	}
	

	
	//게시글 보기
	@GetMapping("/recruitmentView")
	public String view(Recruitment recruitment,Model model
						,@RequestParam("view")int view
						,String comId) 
	{
	    System.out.println("게시글 보기");
	    // 회사 서포트 불러오기
	    CompanySupport support = comService.selectSupport(comId);
	    
	    model.addAllAttributes(recruService.viewRecruId(view));
	    model.addAttribute("comSupport", support);
	    
	    return "/recruitment/recruitmentView"; 
	} 
	
	//게시글 삭제
	@GetMapping("delete")
	public String delete(int no) {
		recruService.deleteRecru(no);
		return "redirect:/recruitment/recruitment"; 
	}
	
	//게시글 수정 화면 이동
	@GetMapping("recruitmentFix")
	public String update(Recruitment recruitment, Model model,@RequestParam("recruNo")int recruNo) {
		model.addAllAttributes(recruService.viewRecruId(recruNo));
		return "recruitment/recruitmentFix";
	}
	
	
	//게시글 수정
	@PostMapping("updateForm")
	public String updateForm(@RequestParam List<MultipartFile> files, @ModelAttribute Recruitment recruitment,HttpSession session)
	{
		
		String typeIdx = "r" + String.valueOf(session.getAttribute("comIdx"));
		
		recruitment.setTypeIdx(typeIdx);
		
		recruService.updateRecru(recruitment,files);
		
		return "redirect:/recruitment/recruitment";
	} 
	
	//게시글 삭제
	
	@GetMapping("deleteForm") 
	public String deleteForm(int view)
	{
		recruService.deleteRecru(view);
		return "redirect:/member/company/hire/list";
	}
	
}
