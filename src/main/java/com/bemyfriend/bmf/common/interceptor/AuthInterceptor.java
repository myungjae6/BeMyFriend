package com.bemyfriend.bmf.common.interceptor;

import java.io.IOException;


import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bemyfriend.bmf.common.code.ConfigCode;

import com.bemyfriend.bmf.common.code.ErrorCode;
import com.bemyfriend.bmf.common.exception.ToAlertException;



//HandlerInterceptor
public class AuthInterceptor implements HandlerInterceptor{
   
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {

     
    
      HttpSession session =request.getSession();
      String[] uriArr = request.getRequestURI().split("/");
      if(uriArr.length > 0) {
    	  
    	  switch(uriArr[1]) {
    	  case "member": 
    		  switch (uriArr[2]) {
	          case "user": 
	              switch (uriArr[3]) {
	              
	              case "login" : if(session.getAttribute("userMember") != null ) {
	            	  				throw new ToAlertException(ErrorCode.AU01);
	            	  				
	              				}else if(session.getAttribute("comMember") != null) {
	              					throw new ToAlertException(ErrorCode.AUTH01);
	              				}
	              					break;
	              				
	              case "loginimpl" : if(session.getAttribute("userMember") != null) {
  	  								throw new ToAlertException(ErrorCode.AU01);
    							}else if(session.getAttribute("comMember") != null) {
	              					throw new ToAlertException(ErrorCode.AUTH01);
	              				}
	              					break;
	             
	              case "mypage": if(session.getAttribute("userMember") == null) { //session에 userMember 속성이 없을 경우 mypage로의 접근을 막음
	            	  				
	            	  				throw new ToAlertException(ErrorCode.U_AUTH01);
	              				}else if(session.getAttribute("comMember") != null) {
	              					throw new ToAlertException(ErrorCode.AUTH01);
	              				}
	             					break;
	              case "join": if(session.getAttribute("userMember") != null) { //session에 persistUser 속성이 없을 경우 joinimpl로의 접근을 막음
          			
			  	  				throw new ToAlertException(ErrorCode.AUTH01);
			    				}else if(session.getAttribute("comMember") != null) {
			    					throw new ToAlertException(ErrorCode.AUTH01);
			    				}
			    					break;
	              case "joinimpl": if(session.getAttribute("persistUser") == null) { //session에 persistUser 속성이 없을 경우 joinimpl로의 접근을 막음
	            			
	            	  				throw new ToAlertException(ErrorCode.AUTH02);
	              				}else if(session.getAttribute("comMember") != null) {
	              					throw new ToAlertException(ErrorCode.AUTH01);
	              				}
	              					break;
	              case "resume": 
	            	  switch(uriArr[4]) {
		            	  case "create" : 
		            		  if(session.getAttribute("userMember") == null) { //session에 userMember 속성이 없을 경우 이력서 작성페이지로의 접근을 막음
	  								throw new ToAlertException(ErrorCode.U_AUTH01);
			    				}else if(session.getAttribute("comMember") != null) {
	            					throw new ToAlertException(ErrorCode.AUTH01);
	            				}
			    					break;
		            	  case "upload" :
		            		  if(session.getAttribute("userMember") == null) { //session에 userMember 속성이 없을 경우 이력서 작성페이지로의 접근을 막음
									throw new ToAlertException(ErrorCode.U_AUTH01);
			    				}else if(session.getAttribute("comMember") != null) {
	          					throw new ToAlertException(ErrorCode.AUTH01);
	          				}
		            		  break;
		            	  case "delete" :
		            		  if(session.getAttribute("userMember") == null) { //session에 userMember 속성이 없을 경우 이력서 작성페이지로의 접근을 막음
									throw new ToAlertException(ErrorCode.U_AUTH01);
			    				}else if(session.getAttribute("comMember") != null) {
	          					throw new ToAlertException(ErrorCode.AUTH01);
	          				}
	            		  break;	
		    					
	            	  }			
	              }
	              break;
	              
	          case "company" :
	        	  switch(uriArr[3]) {
	        	  
		        	  case "login" : if(session.getAttribute("comMember") != null ) {
				  	  				throw new ToAlertException(ErrorCode.AU01);
				  	  				
				    				}	else if(session.getAttribute("userMember") != null) {
				    					throw new ToAlertException(ErrorCode.AUTH01);
				    				}
				    					break;
							    				
				     case "loginimpl" : if(session.getAttribute("comMember") != null) {
										throw new ToAlertException(ErrorCode.AU01);
									}	else if(session.getAttribute("userMember") != null) {
				    					throw new ToAlertException(ErrorCode.AUTH01);
				    				}
				    					break;
		        	  
		        	  case "mypage": if(session.getAttribute("comMember") == null) { //session에 userMember 속성이 없을 경우 mypage로의 접근을 막음
		            	  
					  	  				throw new ToAlertException(ErrorCode.C_AUTH01);
					    			}else if(session.getAttribute("userMember") != null) {
		              					throw new ToAlertException(ErrorCode.AUTH01);
		              				}
					   					break;
					   					
		        	  case "join": if(session.getAttribute("comMember") != null) { //session에 persistUser 속성이 없을 경우 joinimpl로의 접근을 막음
		          			
				  	  				throw new ToAlertException(ErrorCode.AUTH01);
				    				}else if(session.getAttribute("userMember") != null) {
				    					throw new ToAlertException(ErrorCode.AUTH01);
				    				}
				    					break;
		        	  case "joinimpl": if(session.getAttribute("persistUser") == null) { //session에 persistUser 속성이 없을 경우 joinimpl로의 접근을 막음
	  			
					  	  				throw new ToAlertException(ErrorCode.AUTH02);
					    			}else if(session.getAttribute("userMember") != null) {
		              					throw new ToAlertException(ErrorCode.AUTH01);
		              				}
					    				break;
		        	  case "hire": if(session.getAttribute("comMember") == null) { //session에 persistUser 속성이 없을 경우 채용공고 작성패이지로의 접근을 막음
					        			
					  	  				throw new ToAlertException(ErrorCode.C_AUTH01);
					    			}else if(session.getAttribute("userMember") != null) {
		              					throw new ToAlertException(ErrorCode.AUTH01);
		              				}
					    				break;
		        	  }
	        	  break;
	        	  
	            
	         }
    		
    	  case "recruitment" :
    		  switch (uriArr[2]) {
	    		  case "recruitmentForm" : if(session.getAttribute("comMember") == null) {
	    			  throw new ToAlertException(ErrorCode.C_AUTH01);
	    		  }	
    		  }
    		  
    	 }
    	  
      	
  

      }
      return true;
      
      
      
		    	 
      
      
      
      
      
      
      
      
      
      
      
   }
}