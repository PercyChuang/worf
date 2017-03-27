<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<html>
<head>
<link type="text/css" href="${resourcesPath}/style.css" rel="stylesheet">
</head>
<body>
	<table>
		<form action='${contextPath}/task/edit' method="post">
			<tr>
				<td colspan="2" class="bold">更新表达式</td>
			</tr>
			<tr>
				<td>任务名：</td>
				<td><input type="text" name="name" value="${taskName}" readonly="true"></td>
			</tr>
			<tr>
				<td>原时间表达式：</td>
				<td><input type="text" name="olderCronExpression" value="${olderCronExpression}" disabled="true"></td>
			</tr>
			<tr>
				<td>新时间表达式：</td>
				<td><input type="text" name="cronExpression"></td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="提交" />&nbsp;
					<a href="${contextPath}/task/list">返回</a>
				</td>
			</tr>
		</form>
	</table>
</body>
</html>
