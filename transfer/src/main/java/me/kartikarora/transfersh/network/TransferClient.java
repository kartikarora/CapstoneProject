package me.kartikarora.transfersh.network;

import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/**
 * Developer: chipset
 * Package : me.kartikarora.transfersh.network
 * Project : Transfer.sh
 * Date : 10/6/16
 */
public class TransferClient {

    private static TransferInterface transferInterface = null;

    public static TransferInterface getInterface() {
        if (transferInterface == null) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("https://transfer.sh")
                    .build();
            transferInterface = adapter.create(TransferInterface.class);
        }
        return transferInterface;
    }

    public interface TransferInterface {
        @PUT("/{name}")
        void uploadFile(@Body TypedFile typedFile, @Path("name") String name, ResponseCallback callback);
    }
}
