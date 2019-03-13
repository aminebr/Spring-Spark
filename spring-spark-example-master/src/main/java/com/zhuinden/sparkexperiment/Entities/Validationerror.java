package com.zhuinden.sparkexperiment.Entities;


public class Validationerror {
   private String rowid ;
   private Long number_of_cells;

    public Validationerror(String row , Long nbcells){
        this.rowid = row ;
        this.number_of_cells = nbcells;
    }
    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public Long getNumber_of_cells() {
        return number_of_cells;
    }

    public void setNumber_of_cells(Long number_of_cells) {
        this.number_of_cells = number_of_cells;
    }
}
