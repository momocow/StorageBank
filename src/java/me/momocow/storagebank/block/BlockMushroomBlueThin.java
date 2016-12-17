package me.momocow.storagebank.block;

import java.util.Random;

import me.momocow.general.block.MoCrop;
import me.momocow.general.util.NaturalBushManager;
import me.momocow.storagebank.StorageBank;
import me.momocow.storagebank.creativetab.CreativeTab;
import me.momocow.storagebank.init.ModItems;
import me.momocow.storagebank.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMushroomBlueThin extends MoCrop
{
	public static NaturalBushManager manager;
	private static final String NAME = "BlockMushroomBlueThin";
	private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]
			{
					new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.4D, 0.7D),
					new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.5D, 0.8D),
					new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.9D, 0.9D)
			};
	
	public BlockMushroomBlueThin(){
		super();
		this.setUnlocalizedName(Reference.MOD_ID + "." + NAME);
		this.setCreativeTab(CreativeTab.MO_TAB);
		this.setRegistryName(NAME);
		
		GameRegistry.register(this);
	}
	
	/**
	 * Called in FMLInitializationEvent
	 */
	public void init()
	{
		if(StorageBank.proxy.isRemote)
		{
			manager = NaturalBushManager.get(StorageBank.proxy.getWorld(0), "", this);
		}
		
		this.setSuitableSoilList(Blocks.FARMLAND);
		this.setSuitableSoilList(Blocks.DIRT);
		this.setSuitableSoilList(Blocks.COBBLESTONE);
		this.setSuitableSoilList(Blocks.STONE);
		this.setSuitableSoilList(Blocks.LOG);
		this.setSuitableSoilList(Blocks.LOG2);
		this.setSuitableSoilList(Blocks.GRASS);
		this.setSuitableSoilList(Blocks.PLANKS);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if(this.getAge(state) == this.getMaxAge()) return AABB[2];
		else if(this.getAge(state) >= 10) return AABB[1];
		return AABB[0];
	}
	
	@Override
	public int getMaxGrowthLightness()
	{
		return 12;
	}
	
	@Override
	public int getMinGrowthLightness()
	{
		return 0;
	}
	
	@Override
    protected Item getCrop()
    {
        return ModItems.MushroomBlueThin;
    }
	
	@Override
    protected Item getSeed()
    {
        return ModItems.SorusBlueThin;
    }
	
	@Override
	public int quantityDropped(Random random)
    {
		int i = random.nextInt(100);
		if(i == 0)	//1%
		{
			return 10;
		}
		else if( i < 6)	//5%
		{
			return 5;
		}
		else if(i < 26)	//20%
		{
			return 3;
		}
		
        return 1;	//74%
    }
	
	@Override
	protected float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
	{
		Random r = new Random();
		
		if (worldIn.getWorldInfo().isThundering() && worldIn.canSeeSky(pos))
		{
			return 30f;
					
		}
		else if (worldIn.getWorldInfo().isRaining() && worldIn.canSeeSky(pos))
		{
			return 10f + 16f * r.nextFloat();	//float: 10 ~ 26 for 1/3 ~ 1 chance
		}
		
		return 6f + 2f * r.nextFloat();	//float: 6 ~ 8 for 1/5 ~ 1/4 chance
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		super.grow(worldIn, pos, state);
		
		if(rand.nextInt(5) == 0)
		{
			generateGiantMushroom(worldIn, pos, pos);
		}
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            java.util.List<ItemStack> items = getDrops(worldIn, pos, state, fortune);
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

            for (ItemStack item : items)
            {
                if (worldIn.rand.nextFloat() <= chance)
                {
                    spawnAsEntity(worldIn, pos, item);
                    manager.removeBush(pos);
                }
            }
        }
    }
	
	@Override
	public boolean plantToSoil(World worldIn, BlockPos pos){
		if(super.plantToSoil(worldIn, pos))
		{
			manager.addBush(pos);
			return true;
		}
		return false;
	}
	
	//TODO
	public void generateGiantMushroom(World worldIn, BlockPos pos, BlockPos state)
	{
//		orignal in BlockMushrooms.generateBigMushroom(...)
		
//		worldIn.setBlockToAir(pos);
//        WorldGenerator worldgenerator = null;
//
//        if (this == Blocks.BROWN_MUSHROOM)
//        {
//            worldgenerator = new WorldGenBigMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
//        }
//        else if (this == Blocks.RED_MUSHROOM)
//        {
//            worldgenerator = new WorldGenBigMushroom(Blocks.RED_MUSHROOM_BLOCK);
//        }
//
//        if (worldgenerator != null && worldgenerator.generate(worldIn, rand, pos))
//        {
//            return true;
//        }
//        else
//        {
//            worldIn.setBlockState(pos, state, 3);
//            return false;
//        }
	}
}
