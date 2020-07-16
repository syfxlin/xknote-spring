<#import "_macro.ftl" as note>
<#assign title="XK-Note注册">
<#assign body_class="xknote-register">
<#include "_layout.ftl">

<@header>
    <section class="navbar-section">
        <img
            class="xknote-icon"
            src="/logo.png"
            alt="XK-Note icon"
        />
        <a href="/" class="btn btn-link text-large">XK-Note</a>
    </section>
    <section class="navbar-section">
        <a href="/" class="btn btn-link">欢迎</a>
        <a href="/login" class="btn btn-link">登录</a>
    </section>
</@header>

<@main>
    <div class="register">
        <h1>XK-Note 注册</h1>
        <form
            action="/register"
            method="post"
            class="form-horizontal"
        >
            <@note.formCsrf />
            <@note.formGroupInput "registerUser.username" "用户名" />
            <@note.formGroupInput "registerUser.nickname" "昵称" />
            <@note.formGroupInput "registerUser.email" "Email地址" "email" />
            <@note.formGroupInput "registerUser.password" "密码" "password"/>
            <@note.formGroupInput "registerUser.password_confirmation" "密码" "password"/>
            <@note.formGroupSubmit "注册" />
        </form>
    </div>
</@main>

<@footer>
    <div class="xknote-copyright bg-gray">
        <span>©</span>
        <a href="https://github.com/syfxlin/xknote">XK-Note</a> By
        <a href="https://ixk.me">Otstar Lin</a>
    </div>
</@footer>