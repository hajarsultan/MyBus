package net.hajar.mybus;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class location
{
  private Double lastLat;
  private Double lastLong;
  private java.util.Date updated;
  private Integer LSN;
  private java.util.Date created;
  private String objectId;
  private Double currentLat;
  private String ownerId;
  private Double currentLong;
  public Double getlastLong()
  {
    return lastLong;
  }

  public void setlastLong( Double lastLong )
  {
    this.lastLong = lastLong;
  }

  public Double getlastLat()
  {
    return lastLat;
  }

  public void setlastLat( Double lastlocation )
  {
    this.lastLat = lastLong;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public Integer getLSN()
  {
    return LSN;
  }

  public void setLSN( Integer LSN )
  {
    this.LSN = LSN;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Double getcurrentLat()
  {
    return currentLat;
  }

  public void setcurrentLat( Double currentLat )
  {
    this.currentLat = currentLat;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Double getcurrentLong()
  {
    return currentLong;
  }

  public void setcurrentLong( Double currentLong )
  {
    this.currentLong = currentLong;
  }

                                                    
  public location save()
  {
    return Backendless.Data.of( location.class ).save( this );
  }

  public Future<location> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<location> future = new Future<location>();
      Backendless.Data.of( location.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<location> callback )
  {
    Backendless.Data.of( location.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( location.class ).remove( this );
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
      Backendless.Data.of( location.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( location.class ).remove( this, callback );
  }

  public static location findById(String id )
  {
    return Backendless.Data.of( location.class ).findById( id );
  }

  public static Future<location> findByIdAsync(String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<location> future = new Future<location>();
      Backendless.Data.of( location.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<location> callback )
  {
    Backendless.Data.of( location.class ).findById( id, callback );
  }

  public static location findFirst()
  {
    return Backendless.Data.of( location.class ).findFirst();
  }

  public static Future<location> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<location> future = new Future<location>();
      Backendless.Data.of( location.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<location> callback )
  {
    Backendless.Data.of( location.class ).findFirst( callback );
  }

  public static location findLast()
  {
    return Backendless.Data.of( location.class ).findLast();
  }

  public static Future<location> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<location> future = new Future<location>();
      Backendless.Data.of( location.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<location> callback )
  {
    Backendless.Data.of( location.class ).findLast( callback );
  }

  public static BackendlessCollection<location> find(BackendlessDataQuery query )
  {
    return Backendless.Data.of( location.class ).find( query );
  }

  public static Future<BackendlessCollection<location>> findAsync(BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<location>> future = new Future<BackendlessCollection<location>>();
      Backendless.Data.of( location.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<location>> callback )
  {
    Backendless.Data.of( location.class ).find( query, callback );
  }
}