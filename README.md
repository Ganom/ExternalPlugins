# ExternalPlugins
[![Discord](https://discordapp.com/api/guilds/597985733403475982/widget.png?style=shield)](https://discord.gg/hVPfVAR)
[![GitHub issues](https://img.shields.io/github/issues/Ganom/ExternalPlugins.svg)](https://github.com/Ganom/ExternalPlugins/issues)

External plugins for use with OpenOSRS, this is a seperate entity, not OpenOSRS.

Add this repo to your external plugins manager by clicking the top right + button.
Make sure it looks like this picture below.

![](https://cdn.discordapp.com/attachments/598227510727016493/676194450636013568/unknown.png)

If you'd like to support what I do, feel free to subscribe to my patreon @https://www.patreon.com/ganom

## Delays
All delays values below are in milliseconds.
* __Min__: The absolute minimum delay it can use.
* __Max__: The absolute maximum delay it can use.
* __Target__: The peak of the curve, the value most commonly used.
* __Deviation__: How far it can deviate from target and still remain common.
* __Weighted Distribution__: This will turn the standard bell curve into a right shifted bell curve.

Below is a picture of a 10,000,000 click sample size at settings min=20, max=80, target=50, deviation=5.
Blue is weighted distribution.
Green is standard distribution.
![](https://i.imgur.com/CrRCWa5.png)

## Plugins
* __Auto Clicker__: This plugin will automatically click where you are hovering when you press the hotkey.
* __Auto Pray Flick__: This plugin will attempt to automatically flick your prayers, simply place your cursor over the prayer orb, and press the hotkey.
* __Custom Swapper__: One of the more robust plugins, this allows you to execute prebaked commands, such as Equip, Prayer, Cast, Remove, and so on. You can for instance, equip a set of gear, enable your prayer, then activate special all in one hotkey!
* __Item Dropper__:  Pretty self-explanatory, put the ids of the items you want to drop into the config field, and then hit the hotkey. Voila, pesky items begone.
* __Left Click Cast__: Whenever you're wielding a staff or wand, it will change your Attack entry into a Cast entry, based on what spell you have set in the config. Now you too can become decent at PvP!
* __Never Log__: Activate the plugin, and it will make it so you wont logout until you hit the forced logout timer.
* __Nylo Swapper__: This will change your gear and prayers during the Nylocas fight in Theatre of Blood.
* __Olm Swapper__: This will change your prayers during the Olm encounter at Chambers of Xeric.
* __One Click__: Probably the most popular plugin, this plugin allows you to simplify quite a large variety of tasks by reducing the amount of clicks to... 1. For instance, instead of clicking a bone and using it on the altar, just click the altar, or set a custom one inventory one click in the config. You could for instance set it to use an Apple on an Orange in one click if you desired.

##### If you'd like to see what other plugins I offer, make sure to check my discord channel #Platinum-Info
