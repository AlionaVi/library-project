<html lang="en">
<head>
    <link rel="stylesheet" href="/authors.css" , type="text/css">
</head>
<body>
<div class="container">
    <h2>AUTHORS</h2>
    <ul class="responsive-table">
        <li class="table-header">
            <div class="col col-1">ID</div>
            <div class="col col-2">NAME</div>
            <div class="col col-3">SURNAME</div>
            <div class="col col-4">ID_BOOK</div>
            <div class="col col-5">NAME_BOOK</div>
            <div class="col col-6">GENRE</div>
        </li>
        <#list authors as author>
        <#list author.books as book>
        <li class="table-row">
            <div class="col col-1">${author.id}</div>
            <div class="col col-2">${author.name}</div>
            <div class="col col-3">${author.surname}</div>
            <div class="col col-4">${book.id}</div>
            <div class="col col-5">${book.name}</div>
            <div class="col col-6">${book.genre}</div>
        </li>
        </#list>
        </#list>
</ul>
</div>
</body>
</html>