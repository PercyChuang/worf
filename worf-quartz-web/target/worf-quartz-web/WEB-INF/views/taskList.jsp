<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/base.jsp"%>
<html>
<head>
<link type="text/css" href="${resourcesPath}/style.css" rel="stylesheet">
</head>
<body>
	<a href="${contextPath}/task/new">添加新任务</a>
	<table id="planningJobs">
		<tr>
			<td colspan="8" class="bold">计划中的任务</td>
		</tr>
		<tr>
			<td>任务名</td>
			<td>时间表达式</td>
			<td>BeanId</td>
			<td>状态</td>
			<td>下次执行时间</td>
			<td>备注</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${planningJobs}" var="planningJob">
			<tr>
				<td>${planningJob.name}</td>
				<td>${planningJob.cronExpression}</td>
				<td>${planningJob.beanId}</td>
				<td>${planningJob.status}</td>
				<td>${planningJob.nextFire}</td>
				<td>${planningJob.description}</td>
				<td>
					<c:if test="${planningJob.tempTrigger}">
						&nbsp;
					</c:if>
					<c:if test="${not planningJob.tempTrigger}">
					<c:if test="${planningJob.suspendable}">
						<a href="${contextPath}/task/pause/${planningJob.base64Name}">暂停</a>&nbsp;
					</c:if>
					<c:if test="${planningJob.paused}">
						<a href="${contextPath}/task/resume/${planningJob.base64Name}">恢复</a>&nbsp;
					</c:if>
					<c:if test="${planningJob.deletable}">
						<a href="${contextPath}/task/delete/${planningJob.base64Name}" onclick="if(confirm('确定删除?') == false) return false;">删除</a>&nbsp;
					</c:if>
					<c:if test="${planningJob.executable}">
						<a href="${contextPath}/task/trigger/${planningJob.base64Name}">立即执行一次</a>&nbsp;
					</c:if>
					<c:if test="${planningJob.executable}">
						<a href="${contextPath}/task/edit/name/${planningJob.base64Name}/cron/${planningJob.base64CronExpression}">更新表达式</a>
					</c:if>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</table>
	<table id="executingJobs">
		<tr>
			<td colspan="4" class="bold">运行中的任务</td>
		</tr>
		<tr>
			<td>任务名</td>
			<td>状态</td>
			<td>备注</td>
		</tr>
		<c:forEach items="${executingJobs}" var="executingJob">
			<tr>
				<td>${executingJob.name}</td>
				<td>${executingJob.status}</td>
				<td>${executingJob.description}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
