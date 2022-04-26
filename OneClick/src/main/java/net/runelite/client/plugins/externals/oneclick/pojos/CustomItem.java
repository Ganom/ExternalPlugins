package net.runelite.client.plugins.externals.oneclick.pojos;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Getter
@ToString
public class CustomItem
{
	private final int useThisId;
	private final String useThisName;
	private final int onThisId;
	private final String onThisName;

	public CustomItem(Client client, int useThisId, int onThisId)
	{
		this.useThisId = useThisId;
		this.onThisId = onThisId;
		this.useThisName = client.getItemComposition(useThisId).getName();
		this.onThisName = client.getItemComposition(onThisId).getName();
	}

	public String getTargetString()
	{
		return "<col=ff9040>" + this.getUseThisName() + "</col><col=ffffff> -> <col=ff9040>" + this.getOnThisName() + "</col>";
	}

	public static CustomItem from(Client client, String s)
	{
		try
		{
			var split = s.split(":");
			var useThisId = Integer.parseInt(split[0]);
			var onThisId = Integer.parseInt(split[1]);
			if (useThisId > client.getItemCount() || onThisId > client.getItemCount())
			{
				log.error("{} is out of bounds.", s);
				return null;
			}
			return new CustomItem(client, useThisId, onThisId);
		}
		catch (Exception e)
		{
			log.error("{} is not a valid string.", s);
			return null;
		}
	}
}
