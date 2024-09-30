@param io.zeromagic.unpolydemo.app.AppConfigModel appConfig

<form method="post" up-submit up-fail-scroll="[aria-invalid=true]">
  <article>
    <header><strong>Routing</strong> <a href="/static/up-validate.html" class="instruction">?</a></header>
    <label>
      Context Root
      <input type="text" name="contextRoot" value="${appConfig.getContextRoot().value()}"
        aria-invalid="${appConfig.getContextRoot().ariaInvalid()}" up-validate>
      <small>${appConfig.getContextRoot().message()}</small>
    </label>
  </article>
  <article>
    <header><strong>Runtime</strong>  <a href="/static/up-switch.html" class="instruction">?</a></header>
    <fieldset aria-invalid="${appConfig.getJakartaVersion().ariaInvalid()}"
       aria-describedby="jakartaVersion-validation">
      <legend>Jakarta EE version</legend>
      <label>
        <input type="radio" name="jakartaVersion" value="8" up-switch=".jdk"
               checked="${appConfig.getJakartaVersion().is("8")}"> Jakarta EE 8 (Payara 5)
      </label>
      <label>
        <input type="radio" name="jakartaVersion" value="10" up-switch=".jdk"
               checked="${appConfig.getJakartaVersion().is("10")}"> Jakarta EE 10 (Payara 6)
      </label>
      <label>
        <input type="radio" name="jakartaVersion" value="11" up-switch=".jdk"
               checked="${appConfig.getJakartaVersion().is("11")}"> Jakarta EE 11 (Payara 7)
      </label>
      <small id="jakartaVersion-validation">${appConfig.getJakartaVersion().message()}</small>
    </fieldset>
    <fieldset aria-invalid="${appConfig.getJavaVersion().ariaInvalid()}">
      <legend>Java Language version</legend>
      <label class="jdk" up-show-for="8">
        <input type="radio" name="javaVersion" value="8"
              checked="${appConfig.getJavaVersion().is("8")}">Java 8
      </label>
      <label class="jdk" up-show-for="8 10">
        <input type="radio" name="javaVersion" value="11"
               checked="${appConfig.getJavaVersion().is("11")}">Java 11
      </label>
      <label class="jdk" up-show-for="8 10">
        <input type="radio" name="javaVersion" value="17"
              checked="${appConfig.getJavaVersion().is("17")}">Java 17
      </label>
      <label class="jdk" up-show-for="8 10 11">
        <input type="radio" name="javaVersion" value="21"
              checked="${appConfig.getJavaVersion().is("21")}">Java 21
      </label>
      <small>${appConfig.getJavaVersion().message()}</small>
    </fieldset>
  </article>
  <article>
    <header><strong>Scaling</strong> <a href="/static/up-validate-4.html" class="instruction">?</a></header>
    <fieldset aria-invalid="${appConfig.getScalabilityField().ariaInvalid()}">
      <legend>Scaling type</legend>
      <!-- singleton, blue-green, horizontal scaling -->
      <label>
        <input type="radio" name="scalingType" value="singleton"
               checked="${appConfig.getScalabilityField().is("singleton")}" up-validate="#scaling-options"> Singleton
      </label>
      <label>
        <input type="radio" name="scalingType" value="rolling"
               checked="${appConfig.getScalabilityField().is("rolling")}" up-validate="#scaling-options"> Rolling upgrade
      </label>
      <label>
        <input type="radio" name="scalingType" value="horizontal"
               checked="${appConfig.getScalabilityField().is("horizontal")}" up-validate="#scaling-options"> Horizontal scaling
      </label>
      <small>${appConfig.getScalabilityField().message()}</small>
    </fieldset>
    <hr>
    <div id="scaling-options">
      <label>
        Runtime Size
        <select name="runtimeSize" aria-invalid="${appConfig.getRuntimeSize().ariaInvalid()}">
          @for(var size : appConfig.getAvailableSizes())
            <option value="${size}" selected="${appConfig.getRuntimeSize().is(size.name())}">${size.label()}</option>
          @endfor
        </select>
        <small>${appConfig.getRuntimeSize().message()}</small>
      </label>
@if (appConfig.getScalabilityType() != null)
  @if (appConfig.getScalabilityType().hasDatagrid())
    <label>
      Data Grid
      <select name="datagrid" aria-invalid="${appConfig.getDatagrid().ariaInvalid()}">
        <option value="disabled" selected="${appConfig.getDatagrid().is("disabled")}">Disabled</option>
        <option value="enabled" selected="${appConfig.getDatagrid().is("enabled")}">Enabled</option>
      </select>
      <small>${appConfig.getDatagrid().message()}</small>
    </label>
  @endif
  @if (appConfig.getScalabilityType().hasSwitchoverTime())
    <label>
      Switch-over Time
      <select name="switchoverTime" aria-invalid="${appConfig.getSwitchOverTime().ariaInvalid()}">
        <option value="0" selected="${appConfig.getSwitchOverTime().is("0")}">Immediate</option>
        <option value="60" selected="${appConfig.getSwitchOverTime().is("60")}">Moderate (1 minute)</option>
        <option value="300" selected="${appConfig.getSwitchOverTime().is("120")}">Long (5 minutes)</option>
      </select>
      <small>${appConfig.getSwitchOverTime().message()}</small>
    </label>
  @endif
  @if (appConfig.getScalabilityType().hasReplicas())
    <label>
      Replicas
      <input type="number" name="replicas" value="${appConfig.getReplicas().value()}"
             aria-invalid="${appConfig.getReplicas().ariaInvalid()}"
             required>
      <small>${appConfig.getReplicas().message()}</small>
    </label>
  @endif
@endif
    </div>
    <button type="submit">Save</button>
  </article>

</form>