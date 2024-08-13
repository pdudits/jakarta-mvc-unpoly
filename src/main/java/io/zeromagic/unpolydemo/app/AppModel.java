package io.zeromagic.unpolydemo.app;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;

@RequestScoped
@Named("app")
public class AppModel {
    private String name;
    private List<AppChart> charts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AppChart> getCharts() {
        return charts;
    }

    public void setCharts(List<AppChart> charts) {
        this.charts = charts;
    }
}
