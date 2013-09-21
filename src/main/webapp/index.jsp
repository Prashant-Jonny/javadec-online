<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JavaDec - Online Java Decompiler</title>
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<div class="container">
    <div style="max-width: 600px;">
        <h1>JavaDec
            <small>decompiler prototype</small>
        </h1>

        <div style="display: block; min-height: 100px"></div>

        <form action="/decompile" method="post" enctype="multipart/form-data" class="form-horizontal">
            <input type="file" id="file" name="file">
            <button type="submit" class="btn btn-primary">Decompile</button>
        </form>
    </div>
</div>
</body>
</html>
