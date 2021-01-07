<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
</head>
<script type="text/javascript">
    //javascript页面跳转
    window.location.href = "login.jsp";

    //判断用户是否已经登录
    <%--<c:choose>
        <c:when test="${loginUser != null}">
            window.location.href = "/home/main.do";
        </c:when>
        <c:otherwise>
            window.location.href = "login.jsp";
        </c:otherwise>
    </c:choose>--%>

</script>
<body>
</body>
</html>
