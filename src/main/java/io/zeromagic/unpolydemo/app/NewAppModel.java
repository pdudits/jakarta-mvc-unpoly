package io.zeromagic.unpolydemo.app;


import io.zeromagic.unpolydemo.endpoint.FormField;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("newApp")
@RequestScoped
public class NewAppModel {
    private FormField name = FormField.blank();
    private FormField contextRoot = FormField.blank();

    public FormField getName() {
        return name;
    }

    public void setName(FormField name) {
        this.name = name;
    }

    public FormField getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(FormField contextRoot) {
        this.contextRoot = contextRoot;
    }

    public boolean isInputValid() {
        return name.inputValid() && contextRoot.inputValid();
    }
}
