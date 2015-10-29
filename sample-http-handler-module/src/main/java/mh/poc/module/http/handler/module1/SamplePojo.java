package mh.poc.module.http.handler.module1;


public class SamplePojo {
    private String name;
    private int anInt;
    private String description;


    public SamplePojo() {
    }

    public SamplePojo(String name, int anInt, String description) {
        this.name = name;
        this.anInt = anInt;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getAnInt() {
        return anInt;
    }

    public String getDescription() {
        return description;
    }
}
