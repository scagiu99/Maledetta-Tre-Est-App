/*************************************** MAPS ************************************/
function onCreateMapScreen() {
  showScreen("#MapScreen");
  let direction = localStorage.getItem("direction");
  $("#mapTitle").text(direction);

  let lat = model.lastStation().lat;
  let lon = model.lastStation().lon;
  showMap(lat, lon);
}

function showMap(lat, lon) {
  mapboxgl.accessToken =
    "pk.eyJ1IjoiZ2l1bHM5OSIsImEiOiJja2hhbDNudzgxZmo3MnhvNTZ3OXg0MWh4In0.GBbvkSa-AuAiOOxRpdVG7w";
  var map = new mapboxgl.Map({
    container: "map", // container id
    style: "mapbox://styles/mapbox/streets-v11", // style URL
    center: [lon, lat], // starting position [lng, lat]
    zoom: 11, // starting zoom
  });

  showMarkerStations(map);
}

function showMarkerStations(map) {
  let coordinates = [];
  let features = [];

  // creiamo un elemento DOM per il marker
  for (let i = 0; i < model.getSizeStations(); i++) {
    let sname = model.getPositionStation(i).sname;
    let lat = model.getPositionStation(i).lat;
    let lon = model.getPositionStation(i).lon;
    console.log("name " + sname + "lat " + lat + "lon " + lon);

    let linea = {
      type: "Feature",
      geometry: {
        type: "Point",
        coordinates: [lon, lat],
      },
      properties: {
        description: sname,
      },
    };
    features.push(linea);
    coordinates.push([lon, lat]);
    console.log("Coordinate " + coordinates);
  }
  const geojson = {
    type: "FeatureCollection",
    features: features,
  };

  for (const feature of geojson.features) {
    var el = document.createElement("div");
    el.className = "marker";

    marker = new mapboxgl.Marker(el);

    // Aggiungiamo il marker alla mappa
    marker
      .setLngLat(feature.geometry.coordinates)
      .setPopup(
        new mapboxgl.Popup({ offset: 25 }) // add popups
          .setHTML(`<h6>${feature.properties.description}</h6>`)
      )
      .addTo(map);

    el.addEventListener("click", function () {
      console.log("Click sul marker " + marker.description);
    });
  }
  polyline(coordinates, map);
}

//Aggiungo il collegamento tra le linee
function polyline(coordinates, map) {
  map.on("load", () => {
    map.addSource("route", {
      type: "geojson",
      data: {
        type: "Feature",
        properties: {},
        geometry: {
          type: "LineString",
          coordinates: coordinates,
        },
      },
    });
    map.addLayer({
      id: "route",
      type: "line",
      source: "route",
      layout: {
        "line-join": "round",
        "line-cap": "round",
      },
      paint: {
        "line-color": "#FF0000",
        "line-width": 4,
      },
    });
  });

  if (model.getPosition() != undefined) {
    console.log("If di getPosition " + typeof model.getPosition());
    addPositionToMap(map);
  }
}

function addPositionToMap(map) {
  let lon = model.getPosition().lon;
  let lat = model.getPosition().lat;
  let sname = model.getPosition().sname;
  console.log("lon " + model.getPosition().lon);
  var el = document.createElement("div");
  el.style.width = "48px";
  el.style.height = "48px";
  el.style.backgroundImage = `url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAACEElEQVR4Ae3XAWTrQBjA8SEIgmIogqIYhmIoHkowDEXxMAwDFMHwMDw8wBAUw8MARVEUw1AEwzAMRVEMxTAUQ/Dtj2JIe3fZXZeSPz9AtdHL57sDa1VVVQXo4hZjPGKxlmKEASL4KE2nmOADommFIdr4sU4whXzTGE3sLA83EIsyXMF5Ae4hCnc4xzEaOMVI83MenFTDDLLFEmfYVIQVZIvUxUN4eIBs8YFjqLrQ/CesNoAo3EC3MUThClbqQDQcQbdLiEJmazo9Q5TMaumP2O/1G6IpgG4hRFMbhXuEaPpl/VgCQxQqhBj4C93+G64dPoy7hBjI0NI8/xnEQATjJhBDM4SKo7OEGBrAuCdIAe+4RgMHVEOEBFLQCMa9QkoihXFSIot9f4BXGPcOKYlnGLco8PKOEeMMTYRo4AhdXOMeK4iBqasxmmGICB5089HDxOUYjXd0l20hhWzRBdnbGOMd37NrKNSL4uJit/x78wRElvahBlzVMd6DFPk5u0sLruqaj091PcgXcwSwXYg3yFqGtquzmVp+iDpmLt+1w5wvmFkaoyc5xzSFD6vVczbUFWJ4MM3HP2Q55z6Ak5obRuscfRxCVYg/WG5YGepwWoAhJEeGFAn6uFiLMVBckhJ42Fk9zC1tmh38SB76Ba+fU5yjNDVxB1G4RYjSlijO+V6UmP348j9Egr0sAaqqqlz1CVFtt/uKFQ0YAAAAAElFTkSuQmCC')`;

  marker = new mapboxgl.Marker(el);
  // Aggiungiamo il marker alla mappa
  marker
    .setLngLat([lon, lat])
    .setPopup(
      new mapboxgl.Popup({ offset: 25 }) // add popups
        .setHTML(`<h6>${sname}</h6>`)
    )
    .addTo(map);
}
