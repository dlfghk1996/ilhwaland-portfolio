<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
			<c:when test = "${fileType eq 'xlsx' || fileType eq 'xls'}">
				<c:set var="excelMap" value="${fileView.excelMap}"/>
				<ul class="nav nav-tabs">
					<c:forEach var="sheet" items="${excelMap}" varStatus="status">
						<li class="nav-item <c:if test='${status.count eq 1}'>active</c:if>">
							<!-- sheet 이름에 공백이 들어간것을 제거한다. -->
							<a data-toggle="tab" href="#${fn:replace(sheet.key, ' ', '')}">${sheet.key}</a>
						</li>
					</c:forEach>
				</ul>
				<div class="tab-content">
					<c:forEach var="sheet" items="${excelMap}" varStatus="status">
						<table class="table table-bordered table-striped tab-pane fade <c:if test='${status.count eq 1 }'>in active</c:if>" id="${fn:replace(sheet.key, ' ', '')}">
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
           	<c:when test = "${fileType eq 'pdf'}">
             	<div id="pdf">
             		${fileView.sourcePath}
             	</div>
			</c:when>
           <c:otherwise>
           		<div style="width:500px; padding: 10px;">
           			${fileView.sourcePath}
            	</div>
          </c:otherwise>
		</c:choose>
	</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" ></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="text/javascript">
</script>
</body>
</html>