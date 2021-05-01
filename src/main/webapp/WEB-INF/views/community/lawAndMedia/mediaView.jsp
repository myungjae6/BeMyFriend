<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>

  <body>
    
    
    <section class="hero-wrap hero-wrap-2" style="background-image: url('images/bg_2.jpg');" data-stellar-background-ratio="0.5">
      <div class="overlay"></div>
      <div class="container">
        <div class="row no-gutters slider-text align-items-end">
          <div class="col-md-9 ftco-animate pb-5">
          	<p class="breadcrumbs mb-2"><span class="mr-2"><a href="index.html">Home <i class="ion-ios-arrow-forward"></i></a></span> <span class="mr-2"><a href="blog.html">매체 <i class="ion-ios-arrow-forward"></i></a></span></p>
            <h1 class="mb-0 bread">매체</h1>
          </div>
        </div>
      </div>
    </section>

    <section class="ftco-section ftco-degree-bg">
      <div class="container">
        <div class="row">
          <div class="col-lg-8 ftco-animate">
          	<p>
              <img src="images/law/${mediaView.mediaImg}" alt="" class="img-fluid">
            </p>
            <h2 class="mb-3">${mediaView.mediaTitle}</h2>
            <p>${mediaView.mediaContent}</p>
            <div class="tag-widget post-tag-container mb-5 mt-5">
              <div class="tagcloud">
                <a href="#" class="tag-cloud-link">내일</a>
                <a href="#" class="tag-cloud-link">점심은</a>
                <a href="#" class="tag-cloud-link">카레</a>
                <a href="#" class="tag-cloud-link">아님말고</a>
              </div>
            </div>
            
            <div class="about-author d-flex p-4 bg-light">
              <div class="bio mr-5">
                <img src="/resources/law/귀여운팽귄.jpg" alt="Image placeholder" class="img-fluid mb-4">
              </div>
              <div class="desc">
                <h3>관리자</h3> <!-- 나중에 받아올 세션값 -->
               </div>
            </div>
</div>
</div>
</div>
</section>

	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
    
  </body>
</html>