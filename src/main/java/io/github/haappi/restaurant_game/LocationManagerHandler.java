package io.github.haappi.restaurant_game;

public class LocationManagerHandler {

    private final String name;
    private final Building building;

    public LocationManagerHandler(String name, Building building) {
        this.name = name;
        this.building = building;
    }

    public String toString() {
        return name;
    }

    public Building getBuilding() {
        return building;
    }

    public void manage() {
        switch (name) {
            case "View Staff": {
                System.out.println("View Staff");
                break;
            }
            case "View Stock": {
                System.out.println("View Stock");
                break;
            }
            case "Go to Location": {
                System.out.println("Go to Location");
                break;
            }
            case "View Stats": {
                System.out.println("View Stats");
                break;
            }

        }
    }
}
