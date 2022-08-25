package org.techtown.my_jubgging.trashmap;

public class PublicTrashAddress {

    //인덱스 번호
    private Long id;
    //주소
    private String address;
    //쓰레기통 종류
    private String kind;
    //위도
    private String latitude;
    //경도
    private String longitude;
    //설치지점
    private String spec;


    public PublicTrashAddress() {
    }

    public PublicTrashAddress(String address, String kind, String longitude, String latitude, String spec) {
        this.address = address;
        this.kind = kind;
        this.longitude = longitude;
        this.latitude = latitude;
        this.spec = spec;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
