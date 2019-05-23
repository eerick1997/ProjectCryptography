package com.crypto.artist.digitalportrait.PhotoEditor.Principal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crypto.artist.digitalportrait.CryptoUtils.Crypto;
import com.crypto.artist.digitalportrait.PhotoEditor.Adapter.ViewPagerAdapter;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.AddFrameListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.AddTextFragmentListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.BrushFragmentListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.EditImageFragmentListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.EmojiFragmentListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.FilterListFragmentListener;
import com.crypto.artist.digitalportrait.PhotoEditor.Utils.BitmapUtils;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.KeyGenerator;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static com.crypto.artist.digitalportrait.Utilities.Reference.DATE;
import static com.crypto.artist.digitalportrait.Utilities.Reference.EMAIL;
import static com.crypto.artist.digitalportrait.Utilities.Reference.IMAGE;

public class PhotoEditorMain extends AppCompatActivity implements FilterListFragmentListener,
        EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener,
        AddTextFragmentListener, AddFrameListener {

    public static final String pictureName = "meme2.png";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    public static final int CAMERA_REQUEST = 1002;
    private ProgressDialog progressDialog;


    private static final String TAG = "AddOrder";


    private String strEmail;
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 128;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    CoordinatorLayout coordinatorLayout;
    Bitmap originalBitmap, filteredBitmap, finalBitmap;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    CardView btnFiltersList, btnEdit, btnBrush, btnEmoji, btnText, btnAddImage, btnAddFrame, btnCrop;

    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;

    Uri imageSelectedUri;
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_editor);



        strEmail = getIntent().getStringExtra(EMAIL);

        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(), "emojione-android.ttf"))
                .build();
        coordinatorLayout = findViewById(R.id.coordinator);

        btnFiltersList = findViewById(R.id.btn_filters_list);
        btnEdit = findViewById(R.id.btn_edit);
        btnBrush = findViewById(R.id.btn_brush);
        btnEmoji = findViewById(R.id.btn_emoji);
        btnText = findViewById(R.id.btn_text);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnAddFrame = findViewById(R.id.btn_add_frame);
        btnCrop = findViewById(R.id.btn_crop);

        btnFiltersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtersListFragment != null)
                    filtersListFragment.show(getSupportFragmentManager(), filtersListFragment.getTag());
                else {
                    FiltersListFragment filtersListFragment = FiltersListFragment.getInstance(null);
                    filtersListFragment.setListener(PhotoEditorMain.this);
                    filtersListFragment.show(getSupportFragmentManager(), filtersListFragment.getTag());
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(PhotoEditorMain.this);
                editImageFragment.show(getSupportFragmentManager(), editImageFragment.getTag());
            }
        });

        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enable brush mode
                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(PhotoEditorMain.this);
                brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
            }
        });

        btnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(PhotoEditorMain.this);
                emojiFragment.show(getSupportFragmentManager(), emojiFragment.getTag());
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(PhotoEditorMain.this);
                addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());
            }
        });

        btnAddFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameFragment frameFragment = FrameFragment.getInstance();
                frameFragment.setListener(PhotoEditorMain.this);
                frameFragment.show(getSupportFragmentManager(), frameFragment.getTag());
            }
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPicture();
            }
        });
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop(imageSelectedUri);
            }
        });
        loadImage();
    }

    private void startCrop(Uri uri){
        String destinationFileName = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop ucrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        ucrop.start(PhotoEditorMain.this);
    }

    private void addImageToPicture(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_INSERT_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(PhotoEditorMain.this, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                }).check();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);
        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);
        adapter.addFragment(filtersListFragment, "FILTERS");
        adapter.addFragment(editImageFragment, "EDIT");
        viewPager.setAdapter(adapter);
    }

    private void loadImage(){
//        originalBitmap = BitmapUtils.getBitmapFromAssets(this, pictureName, 300, 300);

        byte[] byteArray = getIntent().getByteArrayExtra("image");

        Bitmap decodedByte = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Log.i("IMAGEN",byteArray.toString());
        //byte[] byteArray= new String(Base64.decode("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")).getBytes();
        originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(decodedByte);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onConstraintChanged(float constraint) {
        constrantFinal = constraint;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constraint));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        finalBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFilteredSelected(Filter filter) {
        //resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    private void resetControl(){
        if(editImageFragment != null)
            editImageFragment.resetControls();
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        constrantFinal = 1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_open){
            openImageFromGallery();
            return true;
        } else if(id == R.id.action_save){
            saveImageToGallery();
            return true;
        } else if(id == R.id.action_camera){
            openCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From camera");
                            imageSelectedUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageSelectedUri);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        } else {
                            Toast.makeText(PhotoEditorMain.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void openImageFromGallery(){
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        } else {
                            Toast.makeText(PhotoEditorMain.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void saveImageToGallery(){
        show();
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        photoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                Crypto crypto = new Crypto(PhotoEditorMain.this);
                KeyGenerator keyGenerator = null;
                try {
                    keyGenerator = KeyGenerator.getInstance(ALGORITHM);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                keyGenerator.init(KEY_SIZE);
                byte[] password = keyGenerator.generateKey().getEncoded();
                KeyGenerator IVGenerator = null;
                try {
                    IVGenerator = KeyGenerator.getInstance(ALGORITHM);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                IVGenerator.init(IV_SIZE);
                final byte[][] byteArray = {null};
                byte[] IV = IVGenerator.generateKey().getEncoded();

                CipherParameters IVAndKey = new ParametersWithIV(new KeyParameter(password), IV);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                saveBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byteArray[0] = byteArrayOutputStream.toByteArray();


                //Bitmap comppressedBitmap = BitmapFactory.decodeByteArray(byteArray[0], 0, byteArray[0].length);
                try {
                    byte[] cipherMessage = crypto.encrypt(byteArray[0], IVAndKey);
                    Map<String, Object> order = new HashMap<>();
                    order.put("imageArtist", new String(Base64.encode(cipherMessage)));

                    final byte[]decryptedMessage=crypto.decrypt(cipherMessage,IVAndKey);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decryptedMessage, 0, decryptedMessage.length);


                    crypto.signGenerator(decodedByte);
                    order.put("publicKeyArtist",new String((crypto.getPublicKey())));
                    order.put("signatureArtist",new String((crypto.getSignature())));
                    order.put("estado","Terminado");
                    Log.i("DOCUMENT",getIntent().getExtras().getString("documentName"));

                    db.collection("pedidos").document(getIntent().getExtras().getString("documentName"))
                            .set(order, SetOptions.merge());

                    sendKeyByEmail(password, IV);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Exception e) {

            }
        });
       /* Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    try{
                                        photoEditorView.getSource().setImageBitmap(saveBitmap);
                                        final String path = BitmapUtils.insertImage(getContentResolver(), saveBitmap,
                                                System.currentTimeMillis() + "_profile.png", null);
                                        if (!TextUtils.isEmpty(path)){
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.image_saved,
                                                    Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    openImage(path);
                                                }
                                            });
                                            snackbar.show();
                                        } else {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.image_not_saved,
                                                    Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        } else {
                            Toast.makeText(PhotoEditorMain.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/
    }

    private void openImage(String path){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                imageSelectedUri = data.getData();

                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filtersListFragment = FiltersListFragment.getInstance(originalBitmap);
                filtersListFragment.setListener(this);
            }

            if(requestCode == CAMERA_REQUEST) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, imageSelectedUri, 800, 800);

                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filtersListFragment = FiltersListFragment.getInstance(originalBitmap);
                filtersListFragment.setListener(this);
            } else if (requestCode == PERMISSION_INSERT_IMAGE){
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 200, 200);
                photoEditor.addImage(bitmap);
            } else if(requestCode == UCrop.REQUEST_CROP){
                handleCropResult(data);
            }
        } else if(resultCode == UCrop.RESULT_ERROR){
            handleCropError(data);
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null){
            photoEditorView.getSource().setImageURI(resultUri);
            Bitmap bitmap = ((BitmapDrawable)photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredBitmap = originalBitmap;
            finalBitmap = originalBitmap;
        } else
            Toast.makeText(this, R.string.cannot_retreive_image, Toast.LENGTH_SHORT).show();
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null)
            Toast.makeText(this, "" + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.cannot_retreive_image, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }


    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface, text, color);
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), frame);
        photoEditor.addImage(bitmap);
    }

    private void sendKeyByEmail(byte[] password, byte[] IV) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{strEmail, "vargas.erick030997@gmail.com", "albertoesquivel.97@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Tu llave privada");
        i.putExtra(Intent.EXTRA_TEXT, new String(Base64.encode(password)) + " \n\nvector de inicialización\n\n" + new String(Base64.encode(IV)));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.title_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PhotoEditorMain.this, getString(R.string.not_services_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hide();
    }

    public void show() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    public void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
