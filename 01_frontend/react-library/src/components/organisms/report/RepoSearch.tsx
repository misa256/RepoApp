import {
  Box,
  Button,
  Flex,
  HStack,
  Input,
  Select,
  Text,
  Wrap,
  WrapItem,
} from "@chakra-ui/react";
import axios from "axios";
import { ChangeEvent, useState, FC, useEffect } from "react";
import ReactPaginate from "react-paginate";
import { Report } from "../../../types/api/Report";
import { RepoSearchResultCard } from "../../molecules/report/RepoSearchResultCard";

type props = {
  id: number;
};

export const RepoSearch: FC<props> = (props) => {
  const { id } = props;
  const [date, setDate] = useState<string>("");
  const [place, setPlace] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [places, setPlaces] = useState<string[]>([]);
  const [sortBy, setSortBy] = useState<string>("");
  const [reports, setReports] = useState<Report[]>([]);
  const [errorMessage, setErrorMessage] = useState<string>();
  // ページネーション
  // ページごとに表示させる要素数
  const itemsPerPage = 5;
  // ページごとの一番最初の要素のindex
  const [itemsOffset, setItemsOffset] = useState(0);
  // 次のページの一番最初の要素のindex
  const endOffset = itemsOffset + itemsPerPage;
  // 1ページに表示する要素
  const currentReports = reports.slice(itemsOffset, endOffset);
  // 合計のページ数
  const pageCount = Math.ceil(reports.length / itemsPerPage);
  // ページナンバーがクリックされた時の関数
  const handlePageClick = (e: { selected: number }) => {
    const newOffset = (e.selected * itemsPerPage) % reports.length;
    setItemsOffset(newOffset);
  };
  const placeChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setPlace(e.target.value);
  const titleChange = (e: ChangeEvent<HTMLInputElement>) =>
    setTitle(e.target.value);
  const dateChange = (e: ChangeEvent<HTMLInputElement>) =>
    setDate(e.target.value);
  const sortByChange = (e: ChangeEvent<HTMLSelectElement>) =>
    setSortBy(e.target.value);
  const handleSearch = () => {
    setItemsOffset(0);
    if (errorMessage) {
      setErrorMessage(undefined);
    }
    axios
      .get(
        `http://localhost:8080/api/repo/artist/${id}/reports/search?sortBy=${sortBy}&sortDir=&place=${place}&date=${date}&title=${title}`
      )
      .then((res) => setReports(res.data))
      .catch((error) => {
        setErrorMessage(error.response.data.message);
        console.log(error);
      });
  };
  useEffect(() => {
    // レポートの場所を取得
    axios
      .get(`http://localhost:8080/api/repo/artist/${id}/reports/places`)
      .then((res) => setPlaces(res.data))
      .catch((error) => console.log(error));
    // 特定のアーティストのレポート全件取得
    axios
      .get(`http://localhost:8080/api/repo/artist/${id}/reports`)
      .then((res) => setReports(res.data))
      .catch((error) => setErrorMessage(error.response.data.message));
  }, [id]);
  return (
    <>
      <HStack spacing="40px" mt="10px" mr="20px" ml="20px">
        Ï{/* 日付入力 */}
        <Box w="30%">
          <Input
            type="date"
            value={date}
            onChange={dateChange}
            placeholder="日付"
          />
        </Box>
        {/* 場所入力 */}
        <Select placeholder="場所" onChange={placeChange} w="30%">
          {places.map((option, index) => {
            return (
              <option key={index} value={option}>
                {option}
              </option>
            );
          })}
          　　　　　
        </Select>
        Ï{/* タイトル入力 */}
        <Input
          placeholder="タイトル"
          value={title}
          onChange={titleChange}
          w="30%"
        />
      </HStack>
      <Flex align="center" justify="center" mt="30px">
        <Select placeholder="並び替え" onChange={sortByChange} w="30%">
          <option value="title">タイトル順</option>
          <option value="date">日付順</option>
          <option value="place">場所名順</option>
        </Select>
        <Button colorScheme="whatsapp" onClick={handleSearch}>
          検索
        </Button>
      </Flex>
      {errorMessage ? (
        <Flex align="center" justify="center">
          <Text mt="30px" color="tomato">
            {errorMessage}
          </Text>
        </Flex>
      ) : (
        <>
          <Wrap p={7} spacing="30px">
            {currentReports.map((report, index) => {
              return (
                <WrapItem key={index}>
                  <RepoSearchResultCard report={report} artistId={id} />
                </WrapItem>
              );
            })}
          </Wrap>
          {reports.length > itemsPerPage && (
            <Flex justify="center" align="center">
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
            </Flex>
          )}
        </>
      )}
    </>
  );
};
