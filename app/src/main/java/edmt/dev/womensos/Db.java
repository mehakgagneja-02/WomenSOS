package edmt.dev.womensos;

public class Db {
    String dbId;
    String dbname;
    String dbguardian;
    String dbalternate;
    public Db(){

    }

    public Db(String dbId, String dbname,String dbguardian, String dbalternate){
        this.dbId=dbId;
        this.dbname=dbname;
        this.dbguardian=dbguardian;
        this.dbalternate=dbalternate;


    }
    public String getdbId(){
        return dbId;
    }
    public String getdbname(){
        return dbname;
    }
    public String getdbguardian(){return dbguardian;}
    public String getdbalternate(){return dbalternate;}

}


