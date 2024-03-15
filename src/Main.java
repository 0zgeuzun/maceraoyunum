import java.util.Random;

// Karakter sınıfı
class Character {
    private int health;

    public Character(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int damage) {
        health -= damage;
    }

    public void increaseHealth(int amount) {
        health += amount;
    }

    public boolean isAlive() {
        return health > 0;
    }
}

// Canavar sınıfı
class Monster extends Character {
    private int id;
    private int damage;
    private ItemType dropItem;
    private double dropChance;

    public Monster(int id, int health, int damage, ItemType dropItem, double dropChance) {
        super(health);
        this.id = id;
        this.damage = damage;
        this.dropItem = dropItem;
        this.dropChance = dropChance;
    }

    public int getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public Item attack() {
        Random random = new Random();
        double rand = random.nextDouble();
        if (rand <= dropChance) {
            return new Item(dropItem);
        }
        return null;
    }
}

// Eşya türleri
enum ItemType {
    FOOD, FIREWOOD, WATER, MONEY, WEAPON, ARMOR, NOTHING
}

// Eşya sınıfı
class Item {
    private ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}

// Mekan sınıfı
class Location {
    private String name;
    private Monster[] monsters;
    private ItemType reward;

    public Location(String name, Monster[] monsters, ItemType reward) {
        this.name = name;
        this.monsters = monsters;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public Monster[] getMonsters() {
        return monsters;
    }

    public ItemType getReward() {
        return reward;
    }
}

public class Main {
    public static void main(String[] args) {
        // Karakter oluştur
        Character player = new Character(100);

        // Mekanlar oluştur
        Location cave = new Location("Mağara", new Monster[]{new Monster(1, 20, 5, ItemType.FOOD, 0.5)}, ItemType.FOOD);
        Location forest = new Location("Orman", new Monster[]{new Monster(2, 25, 8, ItemType.FIREWOOD, 0.5)}, ItemType.FIREWOOD);
        Location river = new Location("Nehir", new Monster[]{new Monster(3, 30, 10, ItemType.WATER, 0.5)}, ItemType.WATER);
        Location shop = new Location("Mağaza", null, null); // Mağaza için canavar yok, ödül yok

        // Oyuncunun envanteri
        Item[] inventory = new Item[10];

        // Oyun döngüsü
        Location[] locations = {cave, forest, river, shop};
        for (Location location : locations) {
            System.out.println("Bölge: " + location.getName());

            // Canavarla karşılaşma
            if (location.getMonsters() != null && location.getMonsters().length > 0) {
                Monster monster = location.getMonsters()[0]; // Tek canavarla sınırlı olduğu için sadece ilkini alıyoruz
                System.out.println("Canavarla karşılaşıldı: " + monster.getId());

                // Oyuncu ve canavar sırası
                Random random = new Random();
                boolean playerFirst = random.nextBoolean();

                // Savaş döngüsü
                while (player.isAlive() && monster.isAlive()) {
                    if (playerFirst) {
                        monster.decreaseHealth(10); // Oyuncu saldırıyor
                        System.out.println("Oyuncu saldırdı! Canavarın sağlığı: " + monster.getHealth());
                    } else {
                        player.decreaseHealth(monster.getDamage()); // Canavar saldırıyor
                        System.out.println("Canavar saldırdı! Oyuncunun sağlığı: " + player.getHealth());
                    }
                    playerFirst = !playerFirst; // Sıra değiştir
                }

                // Oyunun sonucuna göre işlem yap
                if (player.isAlive()) {
                    System.out.println("Canavar yenildi! Ödül kazandınız: " + location.getReward());
                    // Ödülü envantere ekle
                    for (int i = 0; i < inventory.length; i++) {
                        if (inventory[i] == null) {
                            inventory[i] = new Item(location.getReward());
                            break;
                        }
                    }
                    // Ödül kazanılan bölgeye tekrar giriş yapılamaz
                    location = null;
                } else {
                    System.out.println("Oyuncu yenildi! Oyun bitti.");
                    break;
                }
            }
        }
    }
}
