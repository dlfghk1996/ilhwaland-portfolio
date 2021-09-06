<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav id="sidebar">
	<div class="sidebar-header">
          <a href="home"><h3>ILHWA LAND</h3></a>
    </div>

    <ul class="list-unstyled components">
    	<p>MEMU</p>
        <li class="active">
            <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">File</a>
            <ul class="collapse list-unstyled" id="homeSubmenu">
                <li>
                    <a href="dataArchive">DB데이터 csv, xlsx 형식으로 내보내기</a>
                </li>
                <li>
                    <a href="fileBoard">웹뷰어 및 업다운로드</a>
                </li>
                <li>
                    <a href="fileConvertPage">파일변환프로그램</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="#">IT 뉴스 크롤링</a>
        </li>
        <li>
            <a href="schedulerPage">스케줄러</a>
        </li>
        <li>
            <a href="#apiSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">오픈 API 활용</a>
            <ul class="collapse list-unstyled" id="apiSubmenu">
                <li>
                    <a href="weather">오늘의 날씨</a>
                </li>
                <li>
                    <a href="car_washMap">내 주변 세차장</a>
                </li>
            </ul>
        </li>
        <li>
            <a href="board">게시판</a>
        </li>
	</ul>
</nav>
