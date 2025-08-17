package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import edu.unlpam.vet.ponzonosos.databinding.ActivityContactUsBinding;
import edu.unlpam.vet.ponzonosos.databinding.ActivityPreventionMeasuresBinding;
import edu.unlpam.vet.ponzonosos.util.Edge;


public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);

        //Enable EdgeToEdge
        Window window = getWindow();
        ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), (v, insets) -> insets);
        WindowCompat.setDecorFitsSystemWindows(window, false);

        Edge.applyDynamicEdgeAppBar(
                this,
                getWindow(),
                binding.iHeader.getRoot(),
                binding.iHeader.llHeader,
                60f
        );

        //boton de more
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu_show_catalog, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.about){
                    navigateToAbout();
                    return true;
                } else if (item.getItemId() == R.id.contact) {
                    navigateToContact();
                    return true;
                }
                return false;
            });
            popup.show();
        });

        // Evento para abrir instagram.
        LinearLayout instagramLayout = findViewById(R.id.llInstagram);

        instagramLayout.setOnClickListener(v ->{
            Uri uri = Uri.parse("http://instagram.com/ponzonosos.lp"); // reemplazá con tu user
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Ver si está instalada la app
            intent.setPackage("com.instagram.android");

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Si no está la app, abrir en navegador
                intent.setPackage(null);
                startActivity(intent);
            }
        });

        // Evento para abrir WhatsApp

        LinearLayout whatsAppLayout = findViewById(R.id.llWhatsApp);

        whatsAppLayout.setOnClickListener(v ->{
            String phoneNumber = "549" + getString(R.string._2302_467189).trim(); // tu número completo
            String message = "Hola, quiero más info sobre la app"; // opcional

            String url = "https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No tenés WhatsApp instalado", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento para abrir mail
        LinearLayout emailLayout = findViewById(R.id.llEmail);

        emailLayout.setOnClickListener(v -> {

            String uriEmail = "mailto:" + getString(R.string.mariabruni_live_com_ar)
                    + "?subject=" + Uri.encode("Consulta desde la app") +
                    "&body=" + Uri.encode("Hola, quería hacer una consulta sobre...");

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(uriEmail));

            try {
                startActivity(Intent.createChooser(intent, "Enviar correo con..."));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No tenés un cliente de correo configurado", Toast.LENGTH_SHORT).show();
            }
        });

        // Evento llamar a Asistencia de toxicológia

        TextView toxicPhone = findViewById(R.id.tvToxicPhone);

        toxicPhone.setOnClickListener(v -> {
            String phoneNumber = getString(R.string._0_800_333_0160); // tu número
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No se pudo abrir la app de teléfono", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  navigateToAbout(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void navigateToContact(){
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }
}
