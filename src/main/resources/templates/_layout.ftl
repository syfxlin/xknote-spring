<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.server.csrf.CsrfToken" -->
<#-- @ftlvariable name="body_class" type="String" -->
<#-- @ftlvariable name="title" type="String" -->
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="csrf-token" content="${_csrf.token}"/>
    <link rel="icon" type="image/x-icon" href="/favicon.ico"/>
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre-exp.min.css"
    />
    <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre-icons.min.css"
    />
    <link
            href="/css/app.css"
            rel="stylesheet"
            type="text/css"
    />
    <link rel="icon" href="/logo.png" sizes="32x32"/>
    <meta
            name="description"
            content="XK-Note | 一个集各种神奇功能的云笔记 | 基于Laravel和Vue开发。"
    />
    <link rel="manifest" href="/manifest.json"/>
    <title>${title}</title>
    <#macro style>
        <#nested>
    </#macro>
</head>
<body class="${body_class}">
<#macro header>
    <header class="navbar xknote-header">
        <#nested>
    </header>
</#macro>
<#macro main>
    <main>
        <#nested>
    </main>
</#macro>
<#macro footer>
    <footer>
        <#nested>
    </footer>
</#macro>
<script src="https://cdn.jsdelivr.net/npm/axios@0.19.0/dist/axios.min.js"></script>
<#macro script>
    <#nested>
</#macro>
<script>
  // if ("serviceWorker" in navigator) {
  //     window.addEventListener("load", () => {
  //         navigator.serviceWorker.register("/sw.js");
  //     });
  // }
</script>
</body>
</html>
