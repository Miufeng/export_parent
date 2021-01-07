<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/12/23 0023
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--第一步： 引入css与js文件--%>
    <link rel="stylesheet" href="../../plugins/ztree/css/demo.css" type="text/css">
    <link rel="stylesheet" href="../../plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="../../plugins/ztree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="../../plugins/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="../../plugins/ztree/js/jquery.ztree.excheck-3.5.js"></script>
    <title>Title</title>
</head>
<body>
<%--第二步： 预留一个位置展示tree--%>
<ul id="treeDemo" class="ztree"></ul>

<script>
    //第三步： 使用js把tree展示出来
    // 1.定义一个全局参数
    var setting = {
        check: {
            enable: true //是否为复选框
        },
        data: {
            simpleData: {
                enable: true  //控制是否有子父节点
            }
        }
    };

    // 2. 获取到tree所要的数据
    var zNodes =[
        { id:1, pId:0, name:"Saas管理", open:true},
        { id:11, pId:1, name:"企业管理"},
        { id:111, pId:1, name:"模块管理"},
    ];

    // 3. 把数据与ul标签、全局参数三者整合一起就形成一颗树
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
</script>
</body>
</html>
