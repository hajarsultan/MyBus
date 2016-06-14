package net.hajar.mybus;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class Company
{
  private String ownerId;
  private java.util.Date updated;
  private java.util.Date created;
  private String name;
  private String objectId;
  private Integer CSN;
  private String address;
  private String phone;
  public String getOwnerId()
  {
    return ownerId;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Integer getCSN()
  {
    return CSN;
  }

  public void setCSN( Integer CSN )
  {
    this.CSN = CSN;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress( String address )
  {
    this.address = address;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone( String phone )
  {
    this.phone = phone;
  }

                                                    
  public Company save()
  {
    return Backendless.Data.of( Company.class ).save( this );
  }

  public Future<Company> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Company> future = new Future<Company>();
      Backendless.Data.of( Company.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Company> callback )
  {
    Backendless.Data.of( Company.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Company.class ).remove( this );
  }

  public Future<Long> removeAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Long> future = new Future<Long>();
      Backendless.Data.of( Company.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Company.class ).remove( this, callback );
  }

  public static Company findById(String id )
  {
    return Backendless.Data.of( Company.class ).findById( id );
  }

  public static Future<Company> findByIdAsync(String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Company> future = new Future<Company>();
      Backendless.Data.of( Company.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Company> callback )
  {
    Backendless.Data.of( Company.class ).findById( id, callback );
  }

  public static Company findFirst()
  {
    return Backendless.Data.of( Company.class ).findFirst();
  }

  public static Future<Company> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Company> future = new Future<Company>();
      Backendless.Data.of( Company.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Company> callback )
  {
    Backendless.Data.of( Company.class ).findFirst( callback );
  }

  public static Company findLast()
  {
    return Backendless.Data.of( Company.class ).findLast();
  }

  public static Future<Company> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Company> future = new Future<Company>();
      Backendless.Data.of( Company.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Company> callback )
  {
    Backendless.Data.of( Company.class ).findLast( callback );
  }

  public static BackendlessCollection<Company> find(BackendlessDataQuery query )
  {
    return Backendless.Data.of( Company.class ).find( query );
  }

  public static Future<BackendlessCollection<Company>> findAsync(BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Company>> future = new Future<BackendlessCollection<Company>>();
      Backendless.Data.of( Company.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Company>> callback )
  {
    Backendless.Data.of( Company.class ).find( query, callback );
  }
}