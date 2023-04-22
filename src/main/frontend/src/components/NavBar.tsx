import React from "react";
import { Link, Box, Flex, Text, Button, Stack } from "@chakra-ui/react";
import { aggregateState } from "../util/json";
import { useAppSelector } from "../hooks";

function NavBar(props: any) {
  const [isOpen, setIsOpen] = React.useState(false);

  const toggle = () => setIsOpen(!isOpen);

  return (
    <NavBarContainer {...props}>
      <Box>Logo</Box>
      <MenuToggle toggle={toggle} isOpen={isOpen} />
      <MenuLinks isOpen={isOpen} />
    </NavBarContainer>
  );
}

function CloseIcon() {
  return (
    <svg width="24" viewBox="0 0 18 18" xmlns="http://www.w3.org/2000/svg">
      <title>Close</title>
      <path
        fill="white"
        d="M9.00023 7.58599L13.9502 2.63599L15.3642 4.04999L10.4142 8.99999L15.3642 13.95L13.9502 15.364L9.00023 10.414L4.05023 15.364L2.63623 13.95L7.58623 8.99999L2.63623 4.04999L4.05023 2.63599L9.00023 7.58599Z"
      />
    </svg>
  );
}

function MenuIcon() {
  return (
    <svg
      width="24px"
      viewBox="0 0 20 20"
      xmlns="http://www.w3.org/2000/svg"
      fill="white"
    >
      <title>Menu</title>
      <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z" />
    </svg>
  );
}

function MenuToggle({
  toggle,
  isOpen,
}: {
  toggle: () => void;
  isOpen: boolean;
}) {
  return (
    <Box display={{ base: "block", md: "none" }} onClick={toggle}>
      {isOpen ? <CloseIcon /> : <MenuIcon />}
    </Box>
  );
}

function MenuItem({
  children,
  isLast = false,
  to = "/",
  ...rest
}: {
  isLast?: boolean;
  to: string;
  children?: React.ReactNode;
  rest?: any[];
}) {
  return (
    <Link href={to}>
      <Text display="block" {...rest}>
        {children}
      </Text>
    </Link>
  );
}

function MenuLinks({ isOpen }: { isOpen: boolean }) {
  const state = useAppSelector((data) => data);
  const downloadSourceCode = async (e: any) => {
    e.preventDefault();
    const fsm = aggregateState(state);
    const resp = await fetch("/generate", {
      body: JSON.stringify(fsm),
      method: "POST",
      headers: { "Content-Type": "text/plain;charset=UTF-8" },
    });
    console.log(resp);
    const data = await resp.blob();
    const downloadAnchor = document.createElement("a");
    downloadAnchor.href = window.URL.createObjectURL(data);
    downloadAnchor.download = state.metaData.className + ".zip";
    downloadAnchor.click();
  };

  return (
    <Box
      display={{ base: isOpen ? "block" : "none", md: "block" }}
      flexBasis={{ base: "100%", md: "auto" }}
    >
      <Stack
        spacing={8}
        align="center"
        justify={["center", "space-between", "flex-end", "flex-end"]}
        direction={["column", "row", "row", "row"]}
        pt={[4, 4, 0, 0]}
      >
        <MenuItem to="/aboutus"> About Us </MenuItem>
        <MenuItem to="/export" isLast>
          <Button size={"sm"} bg="blue.600" onClick={downloadSourceCode}>
            Export
          </Button>
        </MenuItem>
      </Stack>
    </Box>
  );
}

function NavBarContainer({
  children,
  ...props
}: {
  children: React.ReactNode;
}) {
  return (
    <Flex
      as="nav"
      align="center"
      justify="space-between"
      wrap="wrap"
      w="100%"
      p={2}
      bg="main.900"
      color="white"
      borderBottom="1px solid rgba(255, 255, 255, 0.16)"
      {...props}
    >
      {children}
    </Flex>
  );
}

export default NavBar;
