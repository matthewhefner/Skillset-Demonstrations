##############################
#
#	Matt's Weather Data Acquisition and Visualization Project
#
#	This is only the raw source code with minor comments.
#
#	For a full explanation of this project and the results of 
#	the code, please visit http://matthefner.com/randomCreations/weather/index.html
#
##############################


##############################
#First we load the necessary packages and download the XML from the NWS' feed.
##############################

library(XML)
library(ggplot2)
library(ggmap)
library(maps)
library(mapdata)
library(dplyr)
library(akima)
library(plotly)

asc <- function(x) { strtoi(charToRaw(x),16L) }
#Download XMLs
download.file("https://w1.weather.gov/xml/current_obs/index.xml", destfile = ".\\stations.xml")
download.file("https://w1.weather.gov/xml/current_obs/all_xml.zip", destfile = "temp.xml.zip")

##############################
#Then we unzip these files and populate a list of dataframes with observations converted from XML.
##############################

stations <- xmlToDataFrame(".\\stations.xml")
stations <- stations[-c(1,2,3,4,5,6),]
#Unzipping observations
zipF <- "temp.xml.zip"
outDir<-".\\obervations"
unzip(zipF,exdir=outDir)
#Initializing observation list
data <- list()
#Converting XML to data frame
for (i in 1:length(stations$station_id)) {
  tryCatch(
  {
    paste(stations$station_id[i], ".xml", sep="")
    loc <- paste(".\\obervations\\", stations$station_id[i], ".xml", sep="")
    data[[i]] <- xmlToDataFrame(loc)
  },
  error = function(error_message) 
  {
    #Just ignore... NWS' XML formatting isn't always consistient, and mistakes happen...
  }
  )
}
#Create master Weather data frame
Weather <- data.frame(Station = character(), Lon = double(), Lat = double(), Temperature = double(), Humidity = double())
#Populate Weather with NWS observations from near the US
#Observation indexes
#6 - location
#7 - call number
#8 - lon
#9 - lat
for (i in 1:length(stations$station_id)) {
  tryCatch(
    {if (as.numeric(data[[i]][[1]][9]) < -65 && #Longitude and Latitude restraints on observations
         as.numeric(data[[i]][[1]][9]) > -130 &&
         as.numeric(data[[i]][[1]][8]) < 50 &&
         as.numeric(data[[i]][[1]][8]) > 20)
    {
      #Some stations record temperature and humidity at a sligtly different index
      #in their XML; this accounts for that.
      if (as.numeric(asc(substring(data[[i]][[1]][12], 1, 1))) < 58)
      {
        #Some store temp (F) at 13 and humidity at 15
        Obs <- list(Station = data[[i]][[1]][7], Lon = as.numeric(data[[i]][[1]][9]), 
                    Lat = as.numeric(data[[i]][[1]][8]), Temperature = as.numeric(data[[i]][[1]][13]), Humidity = as.numeric(data[[i]][[1]][15]))
        Weather <- rbind(Weather, Obs, stringsAsFactors = FALSE)
      } else {
        #Most others store temp at 14 and humidity at 16
        Obs <- list(Station = data[[i]][[1]][7], Lon = as.numeric(data[[i]][[1]][9]), 
                    Lat = as.numeric(data[[i]][[1]][8]), Temperature = as.numeric(data[[i]][[1]][14]), Humidity = as.numeric(data[[i]][[1]][16]))
        Weather <- rbind(Weather, Obs, stringsAsFactors = FALSE)
      }
    } else {
      #Then the data was not near the US
    }},
    error = function(error_message) 
    {
      #Again, just ignore... NWS' XML formatting isn't always consistient, and mistakes happen...
    }
  )
}

#Get rid of empty cases and imposibilities
Weather <- Weather[complete.cases(Weather),]
Weather <- Weather[Weather$Temperature < 125,]

##############################
#Now, we build national temperature and humidity plots and save them as pngs.
##############################

#Interpolate Temperature data
di <- interp(x = Weather$Lon, y = Weather$Lat, 
             z = Weather$Temperature,
             xo=seq(min(Weather$Lon), max(Weather$Lon), length=400),
             yo=seq(min(Weather$Lat), max(Weather$Lat), length=400),
             duplicate = "mean")
dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
dat_interp <- dat_interp[complete.cases(dat_interp),]

#Temperature Plot
ggplot() + 
  coord_fixed(1.3) + 
  geom_raster(data = dat_interp, aes(x = x, y = y, fill = z), alpha= 0.8, interpolate = TRUE) +
  scale_fill_gradientn(name = "", colors = c("pink", "violet", "purple", "blue", "cyan", "green", "yellow", "orange", "red", "darkred"), space = "Lab",
                       na.value = "grey50", guide = "colourbar", aesthetics = "fill", limits = c(-15,115)) +
  theme_classic() +
  labs(x = "Longitude", y = "Latitude", legend = "", 
       title = "U.S. TEMPERATURE", subtitle = "Fahrenheit") +
  geom_polygon(data = map_data("state"), 
               aes(x=long, y = lat, group = group), 
               fill = NA, color = "black", size = 0.5) +
  theme(legend.position = "top", plot.title = element_text(hjust = 0.5, size = 20), 
        legend.key.width=unit(5,"cm"), plot.subtitle = element_text(hjust = 0.5, size = 10))

#Save image
ggsave('NationalTemperature.png', width = 16, height = 9, dpi = 100)

#Interpolate Humidity data
di <- interp(x = Weather$Lon, y = Weather$Lat, 
             z = Weather$Humidity,
             xo=seq(min(Weather$Lon), max(Weather$Lon), length=400),
             yo=seq(min(Weather$Lat), max(Weather$Lat), length=400),
             duplicate = "mean")
dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
dat_interp <- dat_interp[complete.cases(dat_interp),]

#Humidity Plot
ggplot() + 
  coord_fixed(1.3) + 
  geom_raster(data = dat_interp, aes(x = x, y = y, fill = z), alpha= 0.8, interpolate = TRUE) +
  scale_fill_gradientn(name = "", colors = c("red","yellow", "lightyellow", "lightgreen", "green","forestgreen"), space = "Lab",
                       na.value = "grey50", guide = "colourbar", aesthetics = "fill", limits = c(0,100)) +
  theme_classic() +
  labs(x = "Longitude", y = "Latitude", legend = "", 
       title = "U.S. HUMIDITY", subtitle = "Relative Percent Humidity") +
  geom_polygon(data = map_data("state"), 
               aes(x=long, y = lat, group = group), 
               fill = NA, color = "black", size = 0.5) +
  theme(legend.position = "top", plot.title = element_text(hjust = 0.5, size = 20), 
        legend.key.width=unit(5,"cm"), plot.subtitle = element_text(hjust = 0.5, size = 10))

#Save image
ggsave('NationalHumidity.png', width = 16, height = 9, dpi = 100)

##############################
#Then the interactives, made with the plotly package.
##############################

#Interpolate Temperature data
di <- interp(x = Weather$Lon, y = Weather$Lat, 
             z = Weather$Temperature,
             xo=seq(min(Weather$Lon), max(Weather$Lon), length=40),
             yo=seq(min(Weather$Lat), max(Weather$Lat), length=40),
             duplicate = "mean")
dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
dat_interp <- dat_interp[complete.cases(dat_interp),]

ax <- list(
  visible = FALSE,
  title = "",
  zeroline = FALSE,
  showline = FALSE,
  showticklabels = FALSE,
  showgrid = FALSE
)

#Temperature Plot
plot_ly(x = ~long, y = ~lat, colors = c("white", "pink", "violet", "purple", "blue", "cyan", "green", "yellow", "orange", "red"), xaxis = ax, yaxis = ax,  showlegend = FALSE) %>%
  add_surface(x = dat_interp$x, y= dat_interp$y, z = dat_interp$z, hoverinfo = "none", data = dat_interp) %>%
  add_polygons(data = map_data("county") %>% group_by(group), x = ~long, y = ~lat, hoverinfo = "none", color = I('rgba(100, 100, 100, 0.5)'), fillcolor = 'rgba(7, 164, 181, 0)') %>%
  add_polygons(x = ~long, y = ~lat, hoverinfo = "none", color = I("black"), data = map_data("state") %>% group_by(group), fillcolor = 'rgba(7, 164, 181, 0)') %>%
  add_markers(text = ~paste(Station, "<br />", Temperature), hoverinfo = "text", 
              color = I('rgba(0, 0, 0, 0.2)'), data = Weather, x = ~Lon, y = ~Lat) %>%
  layout(xaxis = ax, yaxis = ax)

#Interpolate Temperature data
di <- interp(x = Weather$Lon, y = Weather$Lat, 
             z = Weather$Humidity,
             xo=seq(min(Weather$Lon), max(Weather$Lon), length=40),
             yo=seq(min(Weather$Lat), max(Weather$Lat), length=40),
             duplicate = "mean")
dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
dat_interp <- dat_interp[complete.cases(dat_interp),]

ax <- list(
  visible = FALSE,
  title = "",
  zeroline = FALSE,
  showline = FALSE,
  showticklabels = FALSE,
  showgrid = FALSE
)

#Temperature Plot
plot_ly(x = ~long, y = ~lat, colors = c("red","yellow", "lightyellow", "lightgreen", "green","forestgreen"), xaxis = ax, yaxis = ax,  showlegend = FALSE) %>%
  add_heatmap(x = dat_interp$x, y= dat_interp$y, z = dat_interp$z, hoverinfo = "none", data = dat_interp) %>%
  add_polygons(data = map_data("county") %>% group_by(group), x = ~long, y = ~lat, hoverinfo = "none", color = I('rgba(100, 100, 100, 0.5)'), fillcolor = 'rgba(7, 164, 181, 0)') %>%
  add_polygons(x = ~long, y = ~lat, hoverinfo = "none", color = I("black"), data = map_data("state") %>% group_by(group), fillcolor = 'rgba(7, 164, 181, 0)') %>%
  add_markers(text = ~paste(Station, "<br />", Humidity), hoverinfo = "text", 
              color = I('rgba(0, 0, 0, 0.2)'), data = Weather, x = ~Lon, y = ~Lat) %>%
  layout(xaxis = ax, yaxis = ax)

##############################
#Then plots for each state.
##############################

#Grab state data
states <- map_data("state")
states$region <- factor(states$region)
names <- levels(states$region)

#Make master state data frame
State = data.frame(Index = numeric(), Name = character(),
    MinLon = numeric(), MaxLon = numeric(), MinLat = numeric(), MaxLat = numeric())

# The 48 cont. US states + DC
for (j in 1:49)
{
  cur_state <- subset(states, region == names[j])
  #Add to Master
  State <- rbind(State, list(Index = j, Name = names[j], MinLon = min(cur_state$long), 
                             MaxLon = max(cur_state$long), MinLat = min(cur_state$lat), MaxLat = max(cur_state$lat)), stringsAsFactors = FALSE)

  #Interpolate state temperature data
  di <- interp(x = Weather$Lon, y = Weather$Lat, 
               z = Weather$Temperature,
               xo=seq(State$MinLon[j], State$MaxLon[j], length=400),
               yo=seq(State$MinLat[j], State$MaxLat[j], length=400),
               duplicate = "mean")
  dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
  dat_interp <- dat_interp[complete.cases(dat_interp),]
  
  #State Temp map
  print(ggplot() + 
    coord_fixed(1.3) +
    geom_raster(data = dat_interp, aes(x = x, y = y, fill = z), alpha= 0.8, interpolate = TRUE) +
    scale_fill_gradientn(name = "", colors = c("pink", "violet", "purple", "blue", "cyan", "green", "yellow", "orange", "red", "darkred"), space = "Lab",
                         na.value = "grey50", guide = "colourbar", aesthetics = "fill", limits = c(-15,115)) +
    theme_classic() +
    labs(x = "Longitude", y = "Latitude", legend = "", 
         title = paste(toupper(names[j]),"TEMPERATURE", sep = " "), subtitle = "Fahrenheit") +
    geom_polygon(data = subset(map_data("county"), region == names[j]), 
                 aes(x=long, y = lat, group = group), 
                 fill = NA, color = "black") + 
    geom_polygon(data = subset(map_data("state"), region == names[j]), 
                 aes(x=long, y = lat, group = group), 
                 fill = NA, color = "black", size = 1) +
    geom_text(data = Weather[Weather$Lon < State$MaxLon[j] & Weather$Lat < State$MaxLat[j] &
                               Weather$Lon > State$MinLon[j] & Weather$Lat > State$MinLat[j],],
              aes(x = Lon, y = Lat, label = Temperature), color = "white", check_overlap = TRUE) +
    theme(legend.position = "top", plot.title = element_text(hjust = 0.5, size = 20), 
          legend.key.width=unit(5,"cm"), plot.subtitle = element_text(hjust = 0.5, size = 10))
  )
  
  #Save image
  ggsave(paste(gsub("[[:space:]]", "_", toupper(names[j])), "_TEMPERATURE.png", sep=""), width = 16, height = 9, dpi = 100)
  
  #Interpolate state humidity data
    di <- interp(x = Weather$Lon, y = Weather$Lat, 
               z = Weather$Humidity,
               xo=seq(State$MinLon[j], State$MaxLon[j], length=400),
               yo=seq(State$MinLat[j], State$MaxLat[j], length=400),
               duplicate = "mean")
    dat_interp <- data.frame(expand.grid(x=di$x, y=di$y), z=c(di$z))
    dat_interp <- dat_interp[complete.cases(dat_interp),]
  
  #State humidity plot
    print(ggplot() + 
    coord_fixed(1.3) +
    geom_raster(data = dat_interp, aes(x = x, y = y, fill = z), alpha= 0.8, interpolate = TRUE) +
      scale_fill_gradientn(name = "", colors = c("red","yellow", "lightyellow", "lightgreen", "green","forestgreen"), space = "Lab",
                       na.value = "grey50", guide = "colourbar", aesthetics = "fill", limits = c(0,100)) +
    theme_classic() +
    labs(x = "Longitude", y = "Latitude", legend = "", 
         title = paste(toupper(names[j]),"HUMIDITY", sep = " "), subtitle = "Fahrenheit") +
    geom_polygon(data = subset(map_data("county"), region == names[j]), 
                 aes(x=long, y = lat, group = group), 
                 fill = NA, color = "black") + 
    geom_polygon(data = subset(map_data("state"), region == names[j]), 
                 aes(x=long, y = lat, group = group), 
                 fill = NA, color = "black", size = 1) +
    geom_text(data = Weather[Weather$Lon < State$MaxLon[j] & Weather$Lat < State$MaxLat[j] &
                               Weather$Lon > State$MinLon[j] & Weather$Lat > State$MinLat[j],],
              aes(x = Lon, y = Lat, label = Humidity), color = "black", check_overlap = TRUE) +
    theme(legend.position = "top", plot.title = element_text(hjust = 0.5, size = 20), 
          legend.key.width=unit(5,"cm"), plot.subtitle = element_text(hjust = 0.5, size = 10))
  )
    
      
  #Save image
  ggsave(paste(gsub("[[:space:]]", "_", toupper(names[j])), "_HUMIDITY.png", sep=""), width = 16, height = 9, dpi = 100)
}