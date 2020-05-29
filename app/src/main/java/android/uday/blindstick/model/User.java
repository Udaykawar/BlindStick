package android.uday.blindstick.model;

public class User {
    private int id;
    private String name;
    private String mobileno;
    private String email;
    private String password;
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }



    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

   public String getMobileno()
   {
       return mobileno;
   }
   public void setMobileno(String mobileno)
   {
       this.mobileno = mobileno;
   }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}

