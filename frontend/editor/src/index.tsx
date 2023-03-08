import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { ChakraProvider, extendTheme } from "@chakra-ui/react";
import { Provider as ReduxProvider } from "react-redux";
import store from "./store";

const colors = {
  primary: {
    100: "#E5FCF1",
    200: "#27EF96",
    300: "#10DE82",
    400: "#0EBE6F",
    500: "#0CA25F",
    600: "#0A864F",
    700: "#086F42",
    800: "#075C37",
    900: "#064C2E",
  },
};

const customTheme = extendTheme({ colors });

const root = ReactDOM.createRoot(document.getElementById("root") as any);
root.render(
  <React.StrictMode>
    <ChakraProvider theme={customTheme}>
      <ReduxProvider store={store}>
        <App />
      </ReduxProvider>
    </ChakraProvider>
  </React.StrictMode>
);
