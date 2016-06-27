package net.hajar.mybus;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataQuery;

public class location
{
  private java.util.Date updated;
  private java.util.Date created;
  private String objectId;
  private Double lat;
  private String ownerId;
  private String bus_number;
  private Double lng;

  public void setlastLong( Double lastLong )
  {
  }

  public void setlastLat( Double lastlocation )
  {
  }

  public Double getLng() {
    return lng;
  }

  public String getBus_number() {
    return bus_number;
  }

  public void setBus_number(String bus_number) {
    this.bus_number = bus_number;
  }

  public Double getLat() {
    return lat;
  }

  public java.util.Date getUpdated()
  {
    return updated;
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
    return lat;
  }

  public void setcurrentLat( Double currentLat )
  {
    this.lat = currentLat;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getcurrentLong()
  {
    return lng;
  }

  public void setcurrentLong( Double currentLong )
  {
    this.lng = currentLong;
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