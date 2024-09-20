package io.zeromagic.unpolydemo.app;

import io.zeromagic.unpolydemo.datatypes.RuntimeSize;
import io.zeromagic.unpolydemo.datatypes.ScalabilityType;
import io.zeromagic.unpolydemo.endpoint.FormField;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.EnumSet;

@RequestScoped
@Named("appConfig")
public class AppConfigModel {
  private FormField contextRoot = FormField.blank();
  private FormField jakartaVersion = FormField.blank();
  private FormField javaVersion = FormField.blank();
  private FormField scalabilityField = FormField.blank();
  private FormField datagrid = FormField.blank();
  private FormField switchOverTime = FormField.blank();
  private FormField replicas = FormField.blank();
  private FormField runtimeSize = FormField.blank();
  private EnumSet<RuntimeSize> availableSizes = EnumSet.noneOf(
      RuntimeSize.class);
  private ScalabilityType scalabilityType;

  public FormField getContextRoot() {
    return contextRoot;
  }

  public void setContextRoot(FormField contextRoot) {
    this.contextRoot = contextRoot;
  }

  public FormField getJakartaVersion() {
    return jakartaVersion;
  }

  public void setJakartaVersion(FormField jakartaVersion) {
    this.jakartaVersion = jakartaVersion;
  }

  public FormField getJavaVersion() {
    return javaVersion;
  }

  public void setJavaVersion(FormField javaVersion) {
    this.javaVersion = javaVersion;
  }

  public FormField getScalabilityField() {
    return scalabilityField;
  }

  public void setScalabilityField(FormField scalabilityField) {
    this.scalabilityField = scalabilityField;
  }

  public FormField getDatagrid() {
    return datagrid;
  }

  public void setDatagrid(FormField datagrid) {
    this.datagrid = datagrid;
  }

  public FormField getSwitchOverTime() {
    return switchOverTime;
  }

  public void setSwitchOverTime(FormField switchOverTime) {
    this.switchOverTime = switchOverTime;
  }

  public FormField getReplicas() {
    return replicas;
  }

  public void setReplicas(FormField replicas) {
    this.replicas = replicas;
  }

  public FormField getRuntimeSize() {
    return runtimeSize;
  }

  public void setRuntimeSize(FormField runtimeSize) {
    this.runtimeSize = runtimeSize;
  }

  public EnumSet<RuntimeSize> getAvailableSizes() {
    return availableSizes;
  }

  public void setAvailableSizes(EnumSet<RuntimeSize> availableSizes) {
    this.availableSizes = availableSizes;
  }

  public boolean inputValid() {
    return FormField.allValid(contextRoot, jakartaVersion, javaVersion,
        scalabilityField,
        datagrid, switchOverTime, replicas, runtimeSize);
  }

  public void setScalabilityType(ScalabilityType scalabilityType) {
    this.scalabilityType = scalabilityType;
  }

  public ScalabilityType getScalabilityType() {
    return scalabilityType;
  }
}
