<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<html>
<head>
<link type="text/css" href="${resourcesPath}/style.css" rel="stylesheet">
</head>
<body>
	<table>
		<form action='${contextPath}/task/new' method="post">
			<tr>
				<td colspan="2" class="bold">添加新任务</td>
			</tr>
			<tr>
				<td>任务名：</td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td>BeanId：</td>
				<td><input type="text" name="beanId"></td>
			</tr>
			<tr>
				<td>时间表达式：</td>
				<td><input type="text" name="cronExpression"></td>
			</tr>
			<tr>
				<td>描述：</td>
				<td><input type="text" name="description"></td>
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
