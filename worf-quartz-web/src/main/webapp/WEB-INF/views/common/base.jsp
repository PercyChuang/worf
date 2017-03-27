<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="contextPath" value="${contextLocation}" />
<c:set var="scriptPath" value="${scriptLocation}" />
<c:set var="stylePath" value="${styleLocation}" />
<c:set var="imagePath" value="${imageLocation}" />
<c:set var="resourcesPath" value="${resourcesLocation}" />
<%
	response.addHeader("Pragma", "no-cache");
	response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
	response.addDateHeader("Expires", 1L);	
%>
