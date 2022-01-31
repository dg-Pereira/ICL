def N = 5
	k = 3 in
	for i = new(0) , *i < k , i := *i + 1 do
		def a = new(0) in
			while *a < N do
				if *a % 2 == 0 then 
					println *a 
				else
					println -*a
				end ;
				a := *a + 1
			end 
		end 
	end
end ;;