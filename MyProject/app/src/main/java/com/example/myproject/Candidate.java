package com.example.myproject;

public class Candidate {
    String id;
    String name;
    String description;
    int voteCount = 0;
    public Candidate(){
    }
    public Candidate(String id, String name, String description, int voteCount){
        this.id = id;
        this.name = name;
        this.description = description;
        this.voteCount = voteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
