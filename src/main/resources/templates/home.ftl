<#-- @ftlvariable name="xknoteData" type="me.ixk.xknote.entity.XknoteData" -->
<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.server.csrf.CsrfToken" -->
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${_csrf.token}">
    <link rel="icon" type="image/x-icon" href="/favicon.ico">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre-exp.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/spectre.css@0.5.8/dist/spectre-icons.min.css">
    <link href="/css/app.css" rel="stylesheet" type="text/css"/>
    <link rel="icon" href="/logo.png" sizes="32x32">
    <meta name="description" content="XK-Note | 一个集各种神奇功能的云笔记 | 基于Laravel和Vue开发。">
    <link rel="manifest" href="/manifest.json">
    <title>XK-Note</title>
</head>
<body>
<div id="app">
    <div>
        <h1>XK-Note</h1>
        <div class="loading loading-lg"></div>
        <p>加载中，请稍后...</p>
    </div>
</div>
<script>
    window.xknote = {
      user_id: "${xknoteData.userId}",
      nickname: "${xknoteData.nickname?js_string}",
      xknote_name: "${xknoteData.xknoteName}",
      document_ext: "${xknoteData.documentExt}"
    }
</script>
<!-- built files will be auto injected -->
<!-- ACE Editor -->
<script src="https://cdn.jsdelivr.net/npm/ace-builds@1.4.4/src-noconflict/ace.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/ace-builds@1.4.4/src-noconflict/ext-language_tools.js"></script>
<!-- Marked -->
<script src="https://cdn.jsdelivr.net/npm/marked@0.7.0/lib/marked.min.js"></script>
<!-- Turndown -->
<script src="https://cdn.jsdelivr.net/npm/turndown@5.0.3/dist/turndown.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/turndown-plugin-gfm@1.0.2/dist/turndown-plugin-gfm.min.js"></script>
<!-- Preitter -->
<script src="https://cdn.jsdelivr.net/npm/prettier@1.18.2/standalone.js"></script>
<script src="https://cdn.jsdelivr.net/npm/prettier@1.18.2/parser-markdown.js"></script>
<!-- Prism.js -->
<script src="/static/prism.js"></script>
<link rel="stylesheet" href="/static/prism-okaidia.css" />
<link rel="stylesheet" href="/static/prism-line-numbers.css" />
<link rel="stylesheet" href="/static/prism-toolbar.css" />
<!-- Katex -->
<script src="https://cdn.jsdelivr.net/npm/katex@0.10.2/dist/katex.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/katex@0.10.2/dist/contrib/auto-render.min.js"></script>
<link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/katex@0.10.2/dist/katex.min.css"
/>
<!-- Mermaid -->
<script src="https://cdn.jsdelivr.net/npm/mermaid@8.4.3/dist/mermaid.min.js"></script>
<!-- Emoji-js -->
<script src="https://cdn.jsdelivr.net/npm/emoji-js@3.4.1/lib/emoji.min.js"></script>
<!-- TinyMCE -->
<script src="https://cdn.jsdelivr.net/npm/tinymce@5.0.5/tinymce.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/tinymce@5.0.5/themes/silver/theme.min.js"></script>
<script src="https://cdn.jsdelivr.net/combine/npm/tinymce@5.0.5/plugins/print/plugin.min.js,npm/tinymce@5.0.5/plugins/preview/plugin.min.js,npm/tinymce@5.0.5/plugins/fullpage/plugin.min.js,npm/tinymce@5.0.5/plugins/fullscreen/plugin.min.js,npm/tinymce@5.0.5/plugins/searchreplace/plugin.min.js,npm/tinymce@5.0.5/plugins/autolink/plugin.min.js,npm/tinymce@5.0.5/plugins/directionality/plugin.min.js,npm/tinymce@5.0.5/plugins/code/plugin.min.js,npm/tinymce@5.0.5/plugins/visualblocks/plugin.min.js,npm/tinymce@5.0.5/plugins/visualchars/plugin.min.js,npm/tinymce@5.0.5/plugins/image/plugin.min.js,npm/tinymce@5.0.5/plugins/link/plugin.min.js,npm/tinymce@5.0.5/plugins/media/plugin.min.js,npm/tinymce@5.0.5/plugins/template/plugin.min.js,npm/tinymce@5.0.5/plugins/codesample/plugin.min.js,npm/tinymce@5.0.5/plugins/table/plugin.min.js,npm/tinymce@5.0.5/plugins/charmap/plugin.min.js,npm/tinymce@5.0.5/plugins/hr/plugin.min.js,npm/tinymce@5.0.5/plugins/pagebreak/plugin.min.js,npm/tinymce@5.0.5/plugins/nonbreaking/plugin.min.js,npm/tinymce@5.0.5/plugins/anchor/plugin.min.js,npm/tinymce@5.0.5/plugins/toc/plugin.min.js,npm/tinymce@5.0.5/plugins/insertdatetime/plugin.min.js,npm/tinymce@5.0.5/plugins/advlist/plugin.min.js,npm/tinymce@5.0.5/plugins/lists/plugin.min.js,npm/tinymce@5.0.5/plugins/wordcount/plugin.min.js,npm/tinymce@5.0.5/plugins/imagetools/plugin.min.js,npm/tinymce@5.0.5/plugins/textpattern/plugin.min.js"></script>
<!-- Diff2Html -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/diff2html@2.12.1/dist/diff2html.min.css">
<script src="https://cdn.jsdelivr.net/npm/diff2html@2.12.1/dist/diff2html.min.js"></script>
<!-- Zooming -->
<script src="https://cdn.jsdelivr.net/npm/zooming@2.1.1/build/zooming.min.js"></script>
<script src="/js/manifest.js"></script>
<script src="/js/vendor.js"></script>
<script src="/js/app.js"></script>
<script>
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('/sw.js');
    });
  }
</script>
</body>
</html>