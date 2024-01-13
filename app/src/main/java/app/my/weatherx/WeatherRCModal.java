package app.my.weatherx;

public class WeatherRCModal {

    private String temperature;
    private String time;
    private String icon_img;
    private String condition_hour;
    private String wind_speed;


    public WeatherRCModal(String temperature, String time, String icon_img, String condition_hour, String wind_speed) {
        this.temperature = temperature;
        this.time = time;
        this.icon_img = icon_img;
        this.condition_hour = condition_hour;
        this.wind_speed = wind_speed;
    }

    public String getCondition_hour() {
        return condition_hour;
    }

    public void setCondition_hour(String condition_hour) {
        this.condition_hour = condition_hour;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon_img() {
        return icon_img;
    }

    public void setIcon_img(String icon_img) {
        this.icon_img = icon_img;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }
}
