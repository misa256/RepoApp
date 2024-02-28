import { Box } from "@chakra-ui/react";
import { Input } from "@chakra-ui/react";
import { Text } from "@chakra-ui/react";
import { ChangeEvent, FC, useEffect, useState } from "react";
import { Flex, Spacer } from "@chakra-ui/react";
import { Stack, HStack, VStack } from "@chakra-ui/react";
import { Select } from "@chakra-ui/react";
import axios from "axios";
import { Button, ButtonGroup } from "@chakra-ui/react";
import { error } from "console";
import { Artist } from "../../../types/api/Artist";
import { ArtistSearchResultCard } from "../../molecules/artist/ArtistSearchResultCard";
import '../../../css/pagenation.css';
import ReactPaginate from "react-paginate";

export const ArtistSearch: FC = () => {
  const [countryOptions, setCountryOptions] = useState<string[]>([]);
  const [agencyOptions, setAgencyOptions] = useState<string[]>([]);
  const [artistName, setArtistName] = useState<string>("");
  const [country, setCountry] = useState<string>("");
  const [agency, setAgency] = useState<string>("");
  const [sortBy, setSortBy] = useState<string>("");
  const [resultArtists, setResultArtists] = useState<Artist[]>([]);
  console.log(resultArtists);
  const [errorMessage, setErrorMessage] = useState<string>("");
  // ページネーション用
  // ページごとに表示させる要素数
  const itemsPerPage = 6;
   // ページごとの一番最初の要素のindex
   const [itemsOffset, setItemsOffset] = useState(0);
   // 次のページの一番最初の要素のindex
   const endOffset = itemsOffset + itemsPerPage;
   // 1ページに表示する要素
   const currentArtists = resultArtists.slice(itemsOffset, endOffset);
  console.log(currentArtists);
   // 合計のページ数
   const pageCount = Math.ceil(resultArtists.length / itemsPerPage);
   // ページナンバーがクリックされた時の関数
   const handlePageClick = (e: { selected: number }) => {
     const newOffset = (e.selected * itemsPerPage) % resultArtists.length;
     setItemsOffset(newOffset);
   };
  
  
  const artistNameChange = (e: ChangeEvent<HTMLInputElement>) =>
    setArtistName(e.target.value);
  const countryChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setCountry(e.target.value);
  const agencyChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setAgency(e.target.value);
  const sortByChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setSortBy(e.target.value);
  // 検索ボタンを押した時に行う処理(非同期処理)
  const handleSearch = () => {
    setItemsOffset(0);
    axios
      .get(
        `http://localhost:8080/api/repo/artist/search?name=${artistName}&agencyName=${agency}&country=${country}&sortBy=${sortBy}&sortDir=`
      )
      .then((res) => {
        setResultArtists(res.data);
        setErrorMessage("");
      })
      .catch((error) => setErrorMessage(error.response.data.message));
  };
  // 初回マウント時に行う処理
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/repo/agency/getAllCountries")
      .then((res) => setCountryOptions(res.data))
      .catch((error) => console.log(error));

    axios
      .get("http://localhost:8080/api/repo/agency/getAgencyNames")
      .then((res) => setAgencyOptions(res.data))
      .catch((error) => console.log(error));
  }, []);

  return (
    <>
      <HStack spacing="40px" mt="60px" mr="20px" ml="20px">
        Ï{/* アーティスト名入力 */}
        <Box w="30%">
          <Input
            value={artistName}
            onChange={artistNameChange}
            placeholder="アーティスト名"
          />
        </Box>
        {/* 国名入力 */}
        <Select placeholder="国名" onChange={countryChange} w="30%">
          {countryOptions.map((option, index) => {
            return (
              <option key={index} value={option}>
                {option}
              </option>
            );
          })}
          　　　　　
        </Select>
        Ï{/* 所属事務所名入力 */}
        <Select placeholder="所属事務所名" onChange={agencyChange} w="30%">
          {agencyOptions.map((option, index) => {
            return (
              <option key={index} value={option}>
                {option}
              </option>
            );
          })}
          　　　　　
        </Select>
        Ï
      </HStack>
      <Flex align = "center" justify = "center" mt="30px">
      <Select placeholder="並び替え" onChange={sortByChange} w="30%">
        <option value='name'>アーティスト名順</option>
        <option value='talentAgency.country'>国名順</option>
        <option value='talentAgency.agencyName'>所属事務所名順</option>
        </Select>
        <Button colorScheme="whatsapp"  onClick={handleSearch}>
          検索
        </Button>
      </Flex>
      {errorMessage !== "" ? (
        <Flex align="center" justify="center">
          <Text mt="30px" color="tomato">
            {errorMessage}
          </Text>
        </Flex>
      ) : (
        <VStack mt="30px" spacing="10px">
          { 
          currentArtists.map((artist) => {
            return (
              <Box key={artist.id}>
                <ArtistSearchResultCard
                  id={artist.id}
                  name={artist.name}
                  country={artist.talentAgency.country}
                  agencyName={artist.talentAgency.agencyName}
                />
              </Box>
            );
          })}
          {
            resultArtists.length > itemsPerPage &&
            <ReactPaginate
            pageCount={pageCount}
            onPageChange={handlePageClick}
            nextLabel=">"
            // 現在のページの前後をいくつ表示させるか
            pageRangeDisplayed={3}
            // 先頭と末尾に表示するページ数
            marginPagesDisplayed={2}
            previousLabel="<"
            pageClassName="page-item"
            pageLinkClassName="page-link"
            previousClassName="page-item"
            previousLinkClassName="page-link"
            nextClassName="page-item"
            nextLinkClassName="page-link"
            breakLabel="..."
            breakClassName="page-item"
            breakLinkClassName="page-link"
            containerClassName="pagination"
            activeClassName="active"
          />
          }
        </VStack>
      )}
    </>
  );
};
