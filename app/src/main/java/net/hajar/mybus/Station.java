package net.hajar.mybus;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.io.DoubleWriter;
import com.backendless.persistence.BackendlessDataQuery;

public class Station
{
  private String station_name;
  private Double lat;
  private Double lng;
  private java.util.Date updated;
  private String ownerId;
  private String objectId;
  private java.util.Date created;

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public java.util.Date getCreated()
  {
    return created;
  }


  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public String getStation_name() {
    return station_name;
  }

  public void setStation_name(String station_name) {
    this.station_name = station_name;
  }

  public Station save()
  {
    return Backendless.Data.of( Station.class ).save( this );
  }

  public Future<Station> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Station> future = new Future<Station>();
      Backendless.Data.of( Station.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Station> callback )
  {
    Backendless.Data.of( Station.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Station.class ).remove( this );
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
      Backendless.Data.of( Station.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Station.class ).remove( this, callback );
  }

  public static Station findById(String id )
  {
    return Backendless.Data.of( Station.class ).findById( id );
  }

  public static Future<Station> findByIdAsync(String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Station> future = new Future<Station>();
      Backendless.Data.of( Station.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Station> callback )
  {
    Backendless.Data.of( Station.class ).findById( id, callback );
  }

  public static Station findFirst()
  {
    return Backendless.Data.of( Station.class ).findFirst();
  }

  public static Future<Station> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Station> future = new Future<Station>();
      Backendless.Data.of( Station.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Station> callback )
  {
    Backendless.Data.of( Station.class ).findFirst( callback );
  }

  public static Station findLast()
  {
    return Backendless.Data.of( Station.class ).findLast();
  }

  public static Future<Station> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Station> future = new Future<Station>();
      Backendless.Data.of( Station.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Station> callback )
  {
    Backendless.Data.of( Station.class ).findLast( callback );
  }

  public static BackendlessCollection<Station> find(BackendlessDataQuery query )
  {
    return Backendless.Data.of( Station.class ).find( query );
  }

  public static Future<BackendlessCollection<Station>> findAsync(BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Station>> future = new Future<BackendlessCollection<Station>>();
      Backendless.Data.of( Station.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Station>> callback )
  {
    Backendless.Data.of( Station.class ).find( query, callback );
  }
}