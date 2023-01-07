package net.hellomouse.photonic.tools.powered;

public class PoweredToolConfig {
	private int _eCostHitMob = 0;
	private int _eCostMine = 0;
	private double _attackSpeed = 2.0;
	private int _useCooldown = 0;

	private long damagePowered = 0;
	private long damageUnpowered = 0;
	private float miningSpeedPowered = 1.0f;
	private float miningSpeedUnpowered = 1.0f;

	private long capacity = 4000;
	private long maxInput = 1000;
	private long maxOutput = 1000;

	public PoweredToolConfig() {}

	public PoweredToolConfig energyStorage(long capacity, long maxInput, long maxOutput) {
		this.capacity = capacity;
		this.maxInput = maxInput;
		this.maxOutput = maxOutput;
		return this;
	}

	public PoweredToolConfig energyCostToHit(int cost) {
		this._eCostHitMob = cost;
		return this;
	}

	public PoweredToolConfig energyCostToMine(int cost) {
		this._eCostMine = cost;
		return this;
	}

	public PoweredToolConfig attackSpeed(double attackSpeed) {
		this._attackSpeed = attackSpeed;
		return this;
	}

	public PoweredToolConfig useCooldown(int useCooldown) {
		this._useCooldown = useCooldown;
		return this;
	}

	public PoweredToolConfig attackDamage(long unpowered, long powered) {
		this.damagePowered = powered;
		this.damageUnpowered = unpowered;
		return this;
	}

	public PoweredToolConfig miningSpeed(float unpowered, float powered) {
		this.miningSpeedPowered = powered;
		this.miningSpeedUnpowered = unpowered;
		return this;
	}

	public int getEnergyCostToHitMob() {
		return _eCostHitMob;
	}

	public int getEnergyCostToMine() {
		return _eCostMine;
	}

	public double getAttackSpeed() {
		return _attackSpeed;
	}

	public int getUseCooldown() {
		return _useCooldown;
	}

	public long getDamagePowered() {
		return damagePowered;
	}

	public long getDamageUnpowered() {
		return damageUnpowered;
	}

	public float getMiningSpeedPowered() {
		return miningSpeedPowered;
	}

	public float getMiningSpeedUnpowered() {
		return miningSpeedUnpowered;
	}

	public long getCapacity() {
		return capacity;
	}

	public long getMaxInputRate() {
		return maxInput;
	}

	public long getMaxOutputRate() {
		return maxOutput;
	}
}
