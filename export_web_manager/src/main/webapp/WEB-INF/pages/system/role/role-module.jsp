<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../base.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <base href="${ctx}/">
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
    <link rel="stylesheet" href="plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="plugins/ztree/js/jquery-1.4.4.min.js"></script>
    <script type="text/javascript" src="plugins/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="plugins/ztree/js/jquery.ztree.excheck-3.5.js"></script>

    <SCRIPT type="text/javascript">

    </SCRIPT>
</head>

<body style="overflow: visible;">
<div id="frameContent" class="content-wrapper" style="margin-left:0px;height: 1200px" >
    <section class="content-header">
        <h1>
            菜单管理
            <small>菜单列表</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!-- .box-body -->
        <div class="box box-primary">
            <div class="box-header with-border">
                <h3 class="box-title">角色 [${role.name}] 权限列表</h3>
            </div>

            <div class="box-body">

                <!-- 数据表格 -->
                <div class="table-box">
                    <!--工具栏-->
                    <div class="box-tools text-left">
                        <button type="button" class="btn bg-maroon" onclick="submitCheckedNodes();">保存</button>
                        <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
                    </div>
                    <!--工具栏/-->
                    <!-- 树菜单 -->
                    <form name="icform" id="icform" method="post" action="/system/role/updateRoleModule.do">
                        <input type="hidden" name="roleid" value="${role.id}" id="roleid"/>
                        <input type="hidden" id="moduleIds" name="moduleIds" value=""/>
                        <div class="content_wrap">
                            <%--第二步： 预留一个位置展示tree--%>
                            <div class="zTreeDemoBackground left" style="overflow: visible">
                                <ul id="treeDemo" class="ztree"></ul>
                            </div>
                        </div>

                    </form>
                    <!-- 树菜单 /-->

                </div>
                <!-- /数据表格 -->

            </div>
            <!-- /.box-body -->
        </div>
    </section>
</div>
</body>
<script>
    //创建权限ztree
    $(function () {
        //使用异步获取数据
        $.ajax({
            url:"/system/role/getModuleTree.do",
            data:{roleid:$("#roleid").val()},
            dataType:"json",
            success:function (treeDate) {
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
                /*
                * 格式：[
                    { id:1, pId:0, name:"Saas管理", open:true},
                    { id:11, pId:1, name:"企业管理"},
                    { id:111, pId:1, name:"模块管理"},
                ]
                * */
                var zNodes = treeDate;

                // 3. 把数据与ul标签、全局参数三者整合一起就形成一颗树
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
        });
    });

    
    //保存角色修改权限
    function submitCheckedNodes() {
        //1.找到选中的模块的id，然后拼接成一个字符串  1,2,3,4,5
        //1.1找到所有被选中的节点
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        //定义一个变量保存模块的id字符串
        var moduleIds = "";
        //1.2 遍历所有被选中的节点
        for (var index = 0; index < nodes.length; index++){
            var node = nodes[index]; // node数据格式：{ id:1, pId:0, name:"Saas管理", open:true}
            //最终的字符串格式：1,2,3,4,5
            if (index==nodes.length-1){
                moduleIds+=node.id;
            }else {
                moduleIds+=node.id+",";
            }
        }

        //把module的id字符串设置到表单中
        $("#moduleIds").val(moduleIds);

        //2.提交表单
        $("#icform").submit();
    }
</script>
</html>