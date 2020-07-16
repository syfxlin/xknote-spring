<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.server.csrf.CsrfToken" -->
<#import "/spring.ftl" as spring>

<#macro metaCsrf>
    <meta name="csrf-token" content="${_csrf.token}"/>
</#macro>

<#macro formCsrf>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</#macro>

<#macro formGroupInput path name type="text" class="">
    <@spring.bind path=path />
    <div class="form-group">
        <div class="col-4 col-sm-12">
            <label for="${spring.status.expression}" class="form-label">
                ${name}
            </label>
        </div>
        <div class="col-8 col-sm-12">
            <input
                    id="${spring.status.expression}"
                    type="${type}"
                    class="form-input ${class}"
                    name="${spring.status.expression}"
                    value="${spring.status.value!}"
                    required
                    autocomplete="${spring.status.expression}"
                    autofocus
            />
            <#list spring.status.errorMessages as error>
                <span class="invalid-feedback" role="alert">
                    <strong>${error}</strong>
                </span>
            </#list>
        </div>
    </div>
</#macro>

<#macro formGroupSubmit name>
    <div class="form-group">
        <div class="col-4 col-sm-12"></div>
        <div class="col-8 col-sm-12">
            <button type="submit" class="btn btn-primary">
                ${name}
            </button>
        </div>
    </div>
</#macro>