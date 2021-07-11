<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>viewTemplate</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<!-- Ajax 로 읽어온 내용을 출력하는데 사용되는 HTML 템플릿 -->
	<!-- 컨트롤러로 부터  dispatcher 방식으로 받은 데이터 출력 -->
	
	<c:set var="fileType" value="${fileView.fileType}"/>
	<div id="uploadFileContent">
		<c:choose>
			<c:when test = "${fileType eq 'xlsx'}">
				<c:set var="excelMap" value="${fileView.excelMap}"/>
				<ul class="nav nav-tabs">
					<c:forEach var="sheet" items="${excelMap}" varStatus="status">
						<li class="nav-item <c:if test='${status.count eq 1}'>active</c:if>">
							<a data-toggle="tab" href="#${sheet.key}">${sheet.key}</a>
						</li>
					</c:forEach>
				</ul>
				<div class="tab-content">
					<c:forEach var="sheet" items="${excelMap}" varStatus="status">
						<table class="table table-bordered table-striped tab-pane fade <c:if test='${status.count eq 1 }'>in active</c:if>" id="${sheet.key}">
							<tbody>
								<c:forEach items="${sheet.value}" var="rowlist">
									<tr>
										<c:forEach items="${rowlist}" var="value">
											<td>${value}</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:forEach>
				</div>
			</c:when>
			<c:when test = "${fileType eq 'csv'}">
             	<table class="table table-bordered table-striped">
             		<c:forEach var="line" items="${fileView.csvList}">
						<tr>
							<c:forEach items="${line}" var="value">
								<td>${value}</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:when test = "${fileType eq 'txt'}">
				<textarea cols="50" rows="10">
					<c:forEach var="value" items="${fileView.txtList}">
            			${value}
            		</c:forEach>
				</textarea>
           </c:when>
           <c:otherwise>
           		<div>
           			${fileView.sourcePath}
            	</div>
          </c:otherwise>
		</c:choose>
	</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		
		// pdf 뷰어 
		// 1. pdfobject
		// pdfobject는 pdf.js를 사용하지 않으면 익스플로러(ie)에서는 작동이 불가능하다.
		// <embed>요소에 직접 크기를 지정 하면 인라인 스타일이 항상 파일의 다른 스타일보다 우선하므로 CSS를 통해 요소의 크기를 조정할 수 없습니다. 
		// 따라서 여기에 표시된대로 외부 CSS 규칙을 사용하여 차원을 지정하는 것이 좋습니다.
		var path = "fileBoard/css/index.css";
		$('.viewPdf').on('click',function() {
			//var options = { 
			//		pdfOpenParams: { 
			//			navpanes: 0, 
			//			toolbar: 0, 
			//			statusbar: 0, 
			//			view: "FitV", 
			//			pagemode: "", 
			//			page: 1,
			//			fallbackLink: "<p>PDF VIEWER를 지원하지 않는 브라우저 입니다. Chrom 및 Edge 에서 다시 실행해 주세요.</p>"
			//			} ,
			//		forcePDFJS: true 
			//		}
			var url = '${pageContext.request.contextPath}/resources/pdf/SpringBoot.pdf';
			PDFObject.embed(url, "#my-container");
			//var options = {
			  // 		width: "20rem",
			   	//	height: "20rem",
			   		
				//};
			// 뷰어로 출력할 PDF파일 경로 설정
			// 뷰어 출력할 HTML 태그 설정

	})
</script>
</body>
</html>