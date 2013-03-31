package com.example.locus.dht;


public class TileKey implements de.uniba.wiai.lspi.chord.service.Key {
  private	String	theString ;

  public TileKey(String theString){ 
	this.theString = theString;
  }
  
  public byte[] getBytes(){
      return	this.theString.getBytes () ;
  } 

  public int hashCode(){
   return	this.theString.hashCode() ;
  }

  /* Make sure this thing works */
  public boolean equals(Object o){
    if (o instanceof TileKey) {
    	((TileKey) o).theString.equals(this.theString ) ;
    }
    return	false ;
  }
}