<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>LHH-list</title>
    <script type="text/javascript">
        //修改
        function updateSku(skuId){
            //市场价
            $("#m" + skuId).attr("disabled",false);
            //售价
            $("#p" + skuId).attr("disabled",false);
            //库存
            $("#s" + skuId).attr("disabled",false);
            //限制
            $("#l" + skuId).attr("disabled",false);
            //费用
            $("#f" + skuId).attr("disabled",false);
        }
        //保存
        function addSku(skuId){
            //市场价
            var m = $("#m" + skuId).attr("disabled",true).val();
            //售价
            var p = $("#p" + skuId).attr("disabled",true).val();
            //库存
            var s = $("#s" + skuId).attr("disabled",true).val();
            //限制
            var l = $("#l" + skuId).attr("disabled",true).val();
            //费用
            var f = $("#f" + skuId).attr("disabled",true).val();
            var url = "/sku/addSku.do";
            var params = {"marketPrice" : m , "price" : p , "stock" : s , "upperLimit" : l , "deliveFee" : f , "id" : skuId};
            //var params = '{"marketPrice" : ' + m + ' , "price" : p , "stock" : s , "upperLimit" : l , "deliveFee" : f , "id" : skuId}';

            //异步保存数据到库中
            $.post(url,params,function(data){
                alert(data.message);
            },"json");


        }

    </script>
</head>
<body>
<div class="box-positon">
    <div class="rpos">当前位置: 库存管理 - 列表</div>
    <div class="clear"></div>
</div>
<div class="body-box">
    <form method="post" id="tableForm">
        <table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
            <thead class="pn-lthead">
            <tr>
                <th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
                <th>商品编号</th>
                <th>商品颜色</th>
                <th>商品尺码</th>
                <th>市场价格</th>
                <th>销售价格</th>
                <th>库       存</th>
                <th>购买限制</th>
                <th>运       费</th>
                <th>是否赠品</th>
                <th>操       作</th>
            </tr>
            </thead>
            <tbody class="pn-ltbody">
            <c:forEach items="${skus }" var="sku">
                <tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
                    <td><input type="checkbox" name="ids" value="73"/></td>
                    <td>${sku.productId }</td>
                    <td align="center">${sku.color.name }</td>
                    <td align="center">${sku.size }</td>
                    <td align="center"><input type="text" id="m${sku.id }" value="${sku.marketPrice }" disabled="disabled" size="10"/></td>
                    <td align="center"><input type="text" id="p${sku.id }" value="${sku.price }" disabled="disabled" size="10"/></td>
                    <td align="center"><input type="text" id="s${sku.id }" value="${sku.stock }" disabled="disabled" size="10"/></td>
                    <td align="center"><input type="text" id="l${sku.id }" value="${sku.upperLimit }" disabled="disabled" size="10"/></td>
                    <td align="center"><input type="text" id="f${sku.id }" value="${sku.deliveFee }" disabled="disabled" size="10"/></td>
                    <td align="center">不是</td>
                    <td align="center"><a href="javascript:updateSku('${sku.id }')"  class="pn-opt">修改</a> | <a href="javascript:addSku('${sku.id }')" class="pn-opt">保存</a></td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </form>
</div>
</body>
</html>