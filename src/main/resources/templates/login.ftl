<#-- @ftlvariable name="RequestParameters.error" type="String" -->
<#-- @ftlvariable name="Session.SPRING_SECURITY_LAST_EXCEPTION" type="org.springframework.security.core.AuthenticationException" -->
<#import "_macro.ftl" as note>
<#assign title="XK-Note登录">
<#assign body_class="xknote-login">
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
        <a href="/register" class="btn btn-link">注册</a>
    </section>
</@header>

<@main>
    <div class="login">
        <h1>XK-Note</h1>
        <form
                action="/login"
                method="post"
                class="form-horizontal"
        >
            <@note.formCsrf />
            <div class="form-group">
                <div class="col-4 col-sm-12">
                    <label for="account" class="form-label"
                    >用户名/邮箱</label
                    >
                </div>
                <div class="col-8 col-sm-12">
                    <input
                            id="account"
                            type="text"
                            class="form-input"
                            name="username"
                            required
                            autocomplete="username"
                            autofocus
                    />
                    <#if RequestParameters.error?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
                        <span
                                class="invalid-feedback"
                                role="alert"
                        >
                            <strong>
                                ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
                            </strong>
                        </span>
                    </#if>
                </div>
            </div>
            <div class="form-group">
                <div class="col-4 col-sm-12">
                    <label for="password" class="form-label"
                    >密码</label
                    >
                </div>
                <div class="col-8 col-sm-12">
                    <input
                            id="password"
                            type="password"
                            class="form-input"
                            name="password"
                            required
                            autocomplete="current-password"
                    />
                </div>
            </div>
            <div class="form-group">
                <div class="col-4 col-sm-12"></div>
                <div class="col-8 col-sm-12">
                    <label class="form-switch" for="remember">
                        <input
                                type="checkbox"
                                name="remember-me"
                                id="remember"
                        />
                        <i class="form-icon"></i> 记住我
                    </label>
                </div>
            </div>
            <div class="form-group">
                <div class="col-4 col-sm-12"></div>
                <div class="col-8 col-sm-12">
                    <button type="submit" class="btn btn-primary">
                        登录
                    </button>
                    <!--@if (Route::has('password.request'))-->
                    <!--<a-->
                    <!--    class="btn btn-link"-->
                    <!--    href="{{ route('password.request') }}"-->
                    <!--    >忘记密码?</a-->
                    <!--&gt;-->
                    <!--@endif-->
                </div>
            </div>
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