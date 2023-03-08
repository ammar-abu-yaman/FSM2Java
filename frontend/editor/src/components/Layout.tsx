import React from "react";
import NavBar from "./NavBar";
import { Flex } from "@chakra-ui/react";

function Layout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <NavBar />
      <Flex width={"full"} wrap="nowrap">
        {children}
      </Flex>
    </>
  );
}

export default Layout;
