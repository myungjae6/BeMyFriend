/**
 * 
 */
    let recognition = null;
    function checkCompatibility(){       
 
        recognition = new(window.SpeechRecognition || window.webkitSpeechRecognition) ();
        recognition.lang = "ko";  
        recognition.maxAlternatives = 3; //유사 단어 5개로 제한!!
        if(!recognition)
        {
            alert("확인이 불가합니당");
        }        
}
     
     function startSpeechRecognition(){
     
         var audio = new Audio('/resources/audio/nogod.mp3');
         var audioStupid = new Audio('/resources/audio/stupid.mp3');
         var audioLaugh = new Audio('/resources/audio/laugh.mp3');
         var audioPororong = new Audio('/resources/audio/pororong.mp3');
         
         console.log("시작");
         //recognition.soundstart("입력하세요");
         recognition.addEventListener("speechstart",() =>{
             console.log("음성 녹음 시작");
         });
         recognition.start();
         
         recognition.addEventListener("result", (event) =>{
             console.log("음성 녹음 결과", event.results);
            const text = event.results[0][0].transcript; //가장 confidence(정확도)가 높은 값을 가져옴
            document.getElementById('searchText').value = text;
           switch(text){
           case "네이버":
        	   location.href ="http://www.naver.com";
        	   break;
           case "구글" :
               location.href ="http://www.google.com";
               break;
           case "커뮤니티" :
              audioPororong.play();
        	   location.href ="/community/review/review";
               break;
           case "법률" :
              audioPororong.play();
               location.href ="/community/law";
               break;
           case "매체" :
               location.href ="/community/media";
               break;
           case "수화" :
               location.href ="/sign/signIndex";
               break;
           case "채용" :
               location.href ="/recruitment/recruitment";
           case "공고" :
               location.href ="/recruitment/recruitment";               
               break;
           case "회원가입" :
               audioPororong.play();
               location.href ="/member/join";
               break;
           case "로그인" :
              audioPororong.play();
               location.href ="/member/user/login";
               break;
           case "로그아웃" :
               audioPororong.play();
               location.href ="/member/user/logout";
               break;
           case "바보" :
                 audioStupid.play();
               break;
           case "멍청이" :
                 audioStupid.play();
               break;
           case "오늘 시험 망침":
                  audioLaugh.play();
               break;
           default: 
            	   audio.play();
           } 
        
        });

         recognition.addEventListener("speechend",()=>{
            console.log("음성 녹음 끝"); 
         });     }

     function endSpeechRecognition(){
         recognition.end();
     }

     window.addEventListener('load',checkCompatibility);
