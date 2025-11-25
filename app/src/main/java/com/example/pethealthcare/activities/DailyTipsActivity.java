package com.example.pethealthcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.pethealthcare.R;

import java.util.Random;

public class DailyTipsActivity extends AppCompatActivity {

    private String[] petCareTips = {
            "Provide fresh water for your pet at all times to keep them hydrated. 💧",
            "Take your pet for regular walks to maintain a healthy weight and reduce anxiety. 🚶‍♂🐶",
            "Feed a balanced diet suited to your pet’s age, breed, and health condition. 🍖🥦",
            "Brush your pet’s fur regularly to prevent matting and excessive shedding. 🐕🛁",
            "Keep vaccinations up to date to protect against diseases like rabies and distemper. 💉🐾",
            "Provide a cozy and clean sleeping area to ensure restful sleep. 🛏🐾",
            "Check your pet’s paws for injuries, cracks, or stuck debris after walks. 🐾🔍",
            "Train your pet using positive reinforcement for better behavior and bonding. 🐕🎾",
            "Ensure your pet gets enough playtime to prevent boredom and destructive behavior. 🧸🐾",
            "Schedule regular vet visits to catch health issues early. 🏥🐶",
            "Trim your pet’s nails to prevent discomfort, posture issues, and injuries. ✂🐾",
            "Keep toxic foods like chocolate, onions, grapes, and xylitol away from pets. 🚫🍫",
            "Groom long-haired pets frequently to prevent matting and hairballs. 🐩✂",
            "Provide safe chew toys to maintain dental health and prevent plaque buildup. 🦷🐕",
            "Always use a leash in public places to ensure safety and prevent runaways. 🦮🚶‍♂",
            "Make sure your pet has an ID tag or microchip for easy identification. 🏷🐾",
            "Clean food and water bowls daily to prevent bacteria buildup. 🥣✨",
            "Give your pet plenty of love, attention, and mental stimulation every day! ❤️🐶🐱",
            "Create a comfortable and quiet space for your pet during storms or fireworks. ⛈🐾",
            "Check your pet’s ears for signs of infection, wax buildup, or parasites. 👂🐾",
            "Ensure indoor pets have enough stimulation, toys, and exercise to stay active. 🏡🐾",
            "Slowly introduce new pets to prevent stress and potential aggression. 🐾🐶🐱",
            "Monitor your pet’s weight and adjust their diet and exercise accordingly. ⚖🐾",
            "Reward good behavior with treats, praise, or playtime to reinforce training. 🍖🎉",
            "Avoid overfeeding and stick to portion-controlled meals to prevent obesity. 🚫🍕🐾",
            "Give your pet a bath only when necessary using pet-friendly shampoo to avoid skin irritation. 🛁🐾",
            "Ensure pets have access to shade and fresh air in hot weather to prevent heatstroke. 🌞🐾",
            "Wash pet bedding regularly to remove allergens, dirt, and pests. 🛏✨",
            "Keep household plants that are toxic to pets, like lilies and aloe, out of reach. 🌱🚫🐾",
            "Be mindful of seasonal pet care—protect paws from hot pavement in summer and provide warmth in winter. ❄☀",
            "Socialize your pet with other animals and humans early to improve behavior and confidence. 🐶🐱🐾",
            "Teach basic commands like 'sit,' 'stay,' and 'come' to improve obedience and safety. 🐕🎓",
            "Provide scratching posts for cats to prevent furniture damage and maintain claw health. 🐱🪵",
            "Keep an emergency pet kit with essential supplies like food, water, and medical records. 🚑🐾",
            "Monitor your pet’s bathroom habits; changes may indicate health issues. 🚽🐕",
            "Limit treats to avoid excessive calorie intake while still rewarding good behavior. 🍗🐾",
            "Provide fresh grass or catnip for cats to aid digestion and prevent boredom. 🌿🐱",
            "Rotate toys and activities to keep your pet engaged and mentally stimulated. 🔄🐾",
            "Use pet-safe cleaning products to prevent accidental poisoning. 🚫🧴🐾",
            "Teach your pet to be comfortable with grooming, handling, and vet visits from an early age. ✂🏥🐕",
            "Recognize signs of stress or illness, like excessive licking, hiding, or appetite changes, and consult a vet if needed. 🔍🐶",
            "Encourage gentle pet interactions with children to prevent fear or aggression. 👶🐾",
            "Be patient and consistent with training—it takes time and positive reinforcement! 🕒🐕",
            "Love and care for your pet unconditionally; they are family! ❤️🐾"
    };

    private TextView tipText;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_daily_tips);

        tipText = findViewById(R.id.tipText);
        Button nextTipButton = findViewById(R.id.nextTipButton);

        random = new Random();
        showRandomTip();

        nextTipButton.setOnClickListener(v -> showRandomTip());
    }

    private void showRandomTip() {
        int index = random.nextInt(petCareTips.length);
        tipText.setText(petCareTips[index]);
    }
}