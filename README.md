# SCALP

Special Contributing Area Loading Program

Download the latest release at https://code.usgs.gov/water/SCALP/-/releases

## Description

Special Contributing Area Loading Program (SCALP) is a hydrologic routing program that uses a linear-reservoir-in-series routing method. Inputs to SCALP include a user input file and runoff depth time series. The outputs of SCALP are outflow time series and a user log file. The user input file is a text file, and the runoff depth time series are contained in an Hydrologic Engineering Center Data Storage System (HEC-DSS) file. The output time series are written to an HEC-DSS file, and the user log file is text-based. Special Contributing Areas (SCAs) are a central concept in SCALP. Stormwater, sewer infiltration, and sanitary flows from an SCA are routed through three sewers represented by linear reservoirs in series.

SCAs are made up of lands segments, sanitary flow information, and three linear reservoirs. Information defining SCAs are contained in user input files. Each land segment in an SCA contains three tributary area types. The tributary area types are pervious, impervious, and subsurface. Runoff depth time series are associated with land segments in the user input file. Each land segment requires three runoff depth time series as input. The input runoff depth time series are overland, impervious, and subsurface runoff. Two flow time series, stormwater and sewer infiltration, are computed for each land segment. Stormwater time series are computed by multiplying the overland and impervious runoff depth by pervious and impervious tributary area, respectively. The resulting flow time series are summed at each time to yield stormwater flow. Sewer infiltration time series are computed by multiplying values in the subsurface runoff depth time series by the subsurface tributary area. The total stormwater and sewer infiltration flow from an SCA is the sum of the respective flow time series among the land segments within the SCA. A third component of sewer inflow from an SCA is sanitary flow. Sanitary flows are defined by sanitary information in the user input file. Sanitary flow is the product of hourly, weekday, and monthly flow factors, sanitary flow per person, and the total population in the SCA. Flows from each source (stormwater, sewer infiltration, and sanitary) are tracked through the routing process by the proportion of the contribution to total sewer inflow.

The stormwater, sewer infiltration, and sanitary flows are routed through three sewers for the SCA. The sewers are represented by three linear reservoirs in series. The three sewers are lateral, submain, and main sewers. The sewer characteristics are defined in the user input file. The characteristics for each sewer include a routing constant, maximum flow, a stop store flag, and a split flow. The routing constant is the proportionality constant of the linear relation between reservoir storge and outflow. The maximum flow parameter is the maximum flow that can flow through a sewer. The stop store flag determines if the volume of flow exceeding the maximum flow are lost from the sewer system or stored until they can be released. The split flow parameter defines sewer overflow. Flows through a sewer exceeding the split flow go to sewer overflow and flows below the split flow value are routed through the sewer system. The time series of outflow from the main sewer and overflow from the system are written to an HEC-DSS file. Qualitative descriptions of the SCALP routing process are written to a text-based user log file.

## Authors

Marian Domanski, Henry Doyle

# Additional Information

Any use of trade, firm, or product names is for descriptive purposes only and
does not imply endorsement by the U.S. Government.

## Disclaimer

The disclaimer statement can be found at [DISCLAIMER.md](DISCLAIMER.md)

## License

License information can be found at [LICENSE.md](LICENSE.md)
